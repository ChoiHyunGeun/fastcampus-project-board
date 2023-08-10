package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private String id;
    private Article article;            //게시글(ID)
    private String content;             //내용

    private String createUser;          //생성자
    private LocalDateTime createDate;   //생성일시
    private String updateUser;          //수정자
    private LocalDateTime updateDate;   //수정일시
}
