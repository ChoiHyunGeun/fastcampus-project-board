package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
//    @Autowired private ArticleRepository articleRepository;
//    @Autowired private ArticleCommentRepository articleCommentRepository;
//
//    @DisplayName("delete 테스트")
//    @Test
//    void name() {
//        long preCount = articleRepository.count();
//        long preCommentCount = articleCommentRepository.count();
//
//        //given
//        Article article = articleRepository.findById(1L).orElseThrow();
//
//        //when
//        articleRepository.delete(article);
//        long newCount = articleRepository.count();
//
//        //then
//        assertThat(preCount).isEqualTo(newCount+1);
//    }
}