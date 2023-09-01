package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/articleComment")
@Controller
public class ArticleCommentController {
    private final ArticleCommentService articleCommentService;


}
