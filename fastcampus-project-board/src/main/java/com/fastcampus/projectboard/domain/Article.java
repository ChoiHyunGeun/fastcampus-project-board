package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class Article {
    private Long  id;
    private String title;               //제목
    private String content;             //내용
    private String hashtag;             //해시태그

    private String createUser;          //생성자
    private LocalDateTime createDate;   //생성일시
    private String updateUser;          //수정자
    private LocalDateTime updateDate;   //수정일시
}
