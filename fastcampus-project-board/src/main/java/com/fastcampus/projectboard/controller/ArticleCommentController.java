package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.response.ArticleCommentResponse;
import com.fastcampus.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articleComment")
@Controller
public class ArticleCommentController {
    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public void saveComment(@PathVariable Long articleId){
        /**
         * 1. 게시물 id가져와야함
         * 2. 입력 값 가져와야함
         */

    }
}
