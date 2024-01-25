package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.constant.FormStatus;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleRequest;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/articles") // 해당 경로로 들어오는 요청은 다 여기서 처리
@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;

    /**
     * 게시판 화면
     * @param searchType
     * @param searchValue
     * @param pageable
     * @param map
     * @return
     */
    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType,
                           @RequestParam(required = false) String searchValue,
                           @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
                           ModelMap map
                           ){
        Page<ArticleResponse> article = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), article.getTotalPages());

        map.addAttribute("articles", article);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "articles/index";
    }

    /**
     * 게시글 상세
     * @param articleId
     * @param map
     * @return
     */
    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){

        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));

        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentResponses());
        //다음, 이전 게시물로 이동할 때 필요
        map.addAttribute("totalCount", articleService.getArticleCount());
        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "articles/detail";
    }

    /**
     * 해시태그 게시판 화면
     * @param searchValue
     * @param pageable
     * @param map
     * @return
     */
    @GetMapping("/search-hashtag")
    public String searchArticleHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ){
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "articles/search-hashtag";
    }

    /**
     * 게시글 등록 화면
     * @param map
     * @return
     */
    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    /**
     * 게시글 등록
     * 저장할 땐 id나 auditingFields에 있는 필드들은 자동으로 세팅되기 때문에 여기서 세팅 될 필요 없음
     * @param articleRequest
     * @param boardPrincipal
     * @return
     */
    @PostMapping ("/form")
    public String postNewArticle(
            ArticleRequest articleRequest,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles";
    }

    /**
     * 게시글 수정 화면
     * 특정한 게시물을 update해야되니 PathVariable로 게시글ID 가져와서 수정
     * @param articleId
     * @param map
     * @return
     */
    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    /**
     * 게시글 수정
     * @param articleId
     * @param articleRequest
     * @param boardPrincipal
     * @return
     */
    @PostMapping ("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId,
                                ArticleRequest articleRequest,
                                @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));

        return "redirect:/articles/" + articleId;
    }

    /**
     * 게시글 삭제
     * @param articleId
     * @param boardPrincipal
     * @return
     */
    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal BoardPrincipal boardPrincipal //사용자 인증 정보는 이런식으로 가져와야함
    ) {
        articleService.deleteArticle(articleId, boardPrincipal.getUsername());

        return "redirect:/articles";
    }
}
