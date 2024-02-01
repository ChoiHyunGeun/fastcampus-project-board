package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.Hashtag;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.HashtagRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor //articleRepository의 생성자를 만들고 해당 클래스에 주입시켜주는 역할
@Slf4j
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;

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
            case HASHTAG -> articleRepository.findByHashtagNames(
                            Arrays.stream(searchKeyword.split(" ")).toList(),
                            pageable
                    )
                    .map(ArticleDto::from);
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
        Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());

        Article article = dto.toEntity(userAccount);
        article.addHashtags(hashtags);
        articleRepository.save(article);
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            /**
             * getReferenceById는 참조 값만 가져온 후, 조회된 엔티티의 내부 값이 필요해지는 시점에 lazy loading으로
             * DB를 조회해 값을 가져오도록 동작한다는 것
             */
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            //게시글을 작성한 사용자와 현재 로그인 한 사용자의 정보가 같은지 체크
            if( article.getUserAccount() != null && article.getUserAccount().equals(userAccount) ){
                //not null 필드이기 때문에 방어 로직 추가
                if(dto.title() != null) article.setTitle(dto.title());
                if(dto.content() != null) article.setContent(dto.content());

                //기존에 게시글에 등록되었던 해시태그 조회
                Set<Long> hashtagIds = article.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(Collectors.toUnmodifiableSet());

                //기존에 등록된 해시태그 모두 삭제
                article.clearHashtags();
                //삭제 정보 바로 DB에 반영 > 등록, 수정, 삭제가 일어날 때 hashtag 테이블에 어떻게 반영이 되는지 아직 이해가 안됨 > 중간 테이블에 간접 반영
                //위에 코드에서 clearHashtags()가 실행되면 article_hashtag 테이블에서 해당 게시글에 관련된 해시태그 정보는 사라지는 듯
                articleRepository.flush();

                //어떤 게시물에도 등록되지 않은 해시태그는 hashtag 테이블에서 데이터 삭제
                // > 중간 테이블에서 조회 했을 때, 어떤 게시글에도 해시태그가 등록되어있지 않다면 해시태그 삭제
                hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

                //게시글 본문에서 파싱된 hashtag 새롭게 등록
                Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());
                article.addHashtags(hashtags);
            }
            /**
             * 이 함수는 클래스 레벨의 @Transactional이 선언되어 있기 때문에
             * 변화를 자동으로 감지하여 쿼리를 날려줌으로 save() 함수를 사용할 필요가 없다고 함.
             */
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 수정하는 데 필요한 정보를 찾을 수 없습니다. - dto: {}" ,e.getLocalizedMessage());
        }


    }
    public void deleteArticle(long articleId, String userId) {
        Article article = articleRepository.getReferenceById(articleId);
        Set<Long> hashtagIds = article.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
        articleRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable) {
        if (hashtagName == null || hashtagName.isBlank()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtagNames(List.of(hashtagName), pageable)
                .map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return hashtagRepository.findAllHashtagNames(); // TODO: HashtagService 로 이동을 고려해보자.
    }

    /**
     * 게시글 본문에서 hashtag 추출
     * @param content
     * @return
     */
    private Set<Hashtag> renewHashtagsFromContent(String content) {
        // 게시글 본문에서 파싱된 해시태그
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        // HashtagName으로 DB에서 조회
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent);
        // 실제로 DB에 등록되어 있는 Hashtag는 조회가되어 existingHashtagNames에 담김
        Set<String> existingHashtagNames = hashtags.stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toUnmodifiableSet());

        //DB에서 조회되지 않은 해시태그들은 추가로 hashtags에 넣어서 새로 등록될 수 있도록 하기
        hashtagNamesInContent.forEach(newHashtagName -> {
            if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return hashtags;
    }
}