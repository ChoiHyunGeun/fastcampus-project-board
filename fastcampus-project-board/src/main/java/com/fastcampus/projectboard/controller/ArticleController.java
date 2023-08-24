package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles") // 해당 경로로 들어오는 요청은 다 여기서 처리
@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(@Autowired ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public String articles(ModelMap map, Pageable pageable){
        Page<Article> results = articleRepository.findAll(pageable);
        map.addAttribute("articles", results);
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){
        map.addAttribute("article", "article");
        map.addAttribute("articleComments", List.of());
        return "articles/detail";
    }
}
