package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment>{

    // 검색에 대한 세부 기능 재정의
    // 인터페이스 파일이라 이 안에서 원래 구현을 넣을 수 없지만, Java8부터 가능해짐
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        //선택적으로 검색이 가능하도록 하기 위함
        //listing이 되지 않은 프로퍼티는 검색에서 제외하도록 하기 위해 true로 설정
        bindings.excludeUnlistedProperties(true);

        //원하는 필드 추가
        bindings.including(root.content, root.createDate, root.createUser);

        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createUser).first(StringExpression::containsIgnoreCase);

        /**
         * String이 아니고 Date기 때문에 DateTimeExpression으로 검색해야함
         * 다른 적절한 검사가 없다고 함
         * 이렇게 하면 시 분 초를 동일하게 넣어줘야하는 문제가 있기 때문에 좋은 방법은 아니라고 함
         */
        bindings.bind(root.createDate).first(DateTimeExpression::eq);
    }
}