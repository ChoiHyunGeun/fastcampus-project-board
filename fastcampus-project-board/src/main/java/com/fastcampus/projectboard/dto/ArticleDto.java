package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createDate,
        String createUser,
        LocalDateTime updateDate,
        String updateUser
) {
    /**
     * ArticleDto 객체 반환
     * @param id
     * @param userAccountDto
     * @param title
     * @param content
     * @param hashtag
     * @param createDate
     * @param createUser
     * @param updateDate
     * @param updateUser
     * @return
     */
    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime createDate, String createUser, LocalDateTime updateDate, String updateUser) {
        return new ArticleDto(id, userAccountDto, title, content, hashtag, createDate, createUser, updateDate, updateUser);
    }

    /**
     * 세팅된 ArticleDto 객체 반환
     * @param entity
     * @return
     */
    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreateDate(),
                entity.getCreateUser(),
                entity.getUpdateDate(),
                entity.getUpdateUser()
        );
    }

    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity(),
                title,
                content,
                hashtag
        );
    }

}