package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor //articleRepository의 생성자를 만들고 해당 클래스에 주입시켜주는 역할
@Slf4j
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserId(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId).map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시물이 없습니다. aritlcleId : " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            /**
             * getReferenceById는 참조 값만 가져온 후, 조회된 엔티티의 내부 값이 필요해지는 시점에 lazy loading으로
             * DB를 조회해 값을 가져오도록 동작한다는 것
             */
            Article article = articleRepository.getReferenceById(articleId);

            //not null 필드이기 때문에 방어 로직 추가
            if(dto.title() != null) article.setTitle(dto.title());
            if(dto.content() != null) article.setContent(dto.content());

            article.setHashtag(dto.hashtag());

            /**
             * 이 함수는 클래스 레벨의 @Transactional이 선언되어 있기 때문에
             * 변화를 자동으로 감지하여 쿼리를 날려줌으로 save() 함수를 사용할 필요가 없다고 함.
             */
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}" ,dto);
        }


    }
    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable){
        if(hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    public List<String> getHashtags(){
        return articleRepository.findAllDistinctHashtags();
    }
}