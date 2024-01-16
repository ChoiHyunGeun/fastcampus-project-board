package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource //이 어노테이션이 있어야만 해당 엔티티에 대한 rest api를 사용할 수 있음
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,     //Article 엔티티에 있는 모든 필드에 대한 기본 검색 기능 제공. 대소문자 구분하지 않고 부분 검색은 안된다.
        QuerydslBinderCustomizer<QArticle>      //위에서 안되는 부분 검색이 가능하도록 해줌
{
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserId(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    // 검색에 대한 세부 기능 재정의
    // 인터페이스 파일이라 이 안에서 원래 구현을 넣을 수 없지만, Java8부터 가능해짐
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        //선택적으로 검색이 가능하도록 하기 위함
        //listing이 되지 않은 프로퍼티는 검색에서 제외하도록 하기 위해 true로 설정
        bindings.excludeUnlistedProperties(true);

        //원하는 필드 추가
        bindings.including(root.title, root.content, root.hashtag, root.createDate, root.createUser);

        /**
         * exactMatch로 동작하는데 그걸 바꿈
         * 검색 파라미터는 하나만 받기때문에 first() 사용
         * 결과적으로 like '%${v}%' 이런식의 쿼리가 생성됨
         */
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);

        /**
         * 위에 코드와 다른 점은 쿼리가 다름
         * like '${v}' 이런 쿼리임
         * bindings.bind(root.title).first(StringExpression::likeIgnoreCase);
         */

        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createUser).first(StringExpression::containsIgnoreCase);

        /**
         * String이 아니고 Date기 때문에 DateTimeExpression으로 검색해야함
         * 다른 적절한 검사가 없다고 함
         * 이렇게 하면 시 분 초를 동일하게 넣어줘야하는 문제가 있기 때문에 좋은 방법은 아니라고 함
         */
        bindings.bind(root.createDate).first(DateTimeExpression::eq);
    }
}