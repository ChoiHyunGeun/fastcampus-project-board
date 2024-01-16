package com.fastcampus.projectboard.repository.querydsl;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom{

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    /**
     * article 테이블에서 중복 제거한 hashtag 데이터를 조회
     * @return List<article>  해쉬태그 속성만 가져옴
     */
    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        return from(article)
                .distinct()
                //.select(article) > 이건 article 정보 다 가져오는 것이기 때문에 querydsl 쓸 필요 없음
                .select(article.hashtag) // 하나의 컬럼만 조회하고 싶을 때 이렇게 표현함
                .where(article.hashtag.isNotNull())
                .fetch();
    }
}
