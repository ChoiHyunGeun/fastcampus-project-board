package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.response.ArticleCommentResponse;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles") // 해당 경로로 들어오는 요청은 다 여기서 처리
@Controller
public class ArticleController {
    private final ArticleService articleService;

    private final PaginationService paginationService;
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

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){

        ArticleWithCommentsDto articleWithCommentsDto = articleService.getArticle(articleId);

        map.addAttribute("article", articleWithCommentsDto);
        map.addAttribute("articleComments", articleWithCommentsDto.articleCommentDtos());
        return "articles/detail";
    }

    @PostMapping
    public void saveArticle(@RequestBody ArticleDto dto){
        articleService.saveArticle(dto);
    }
}
