package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createDate,
        String createUser,
        LocalDateTime updateDate,
        String updateUser
) {
    public static ArticleCommentDto of(Long id, Long articleId, UserAccountDto userAccountDto, String content, LocalDateTime createDate, String createUser, LocalDateTime updateDate, String updateUser) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, createDate, createUser, updateDate, updateUser);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreateDate(),
                entity.getCreateUser(),
                entity.getUpdateDate(),
                entity.getUpdateUser()
        );
    }

    public ArticleComment toEntity(Article entity) {
        return ArticleComment.of(
                entity,
                userAccountDto.toEntity(),
                content
        );
    }

}