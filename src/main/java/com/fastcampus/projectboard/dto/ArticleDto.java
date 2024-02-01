package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        Set<HashtagDto> hashtagDtos,
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
     * @param hashtagDtos
     * @param createDate
     * @param createUser
     * @param updateDate
     * @param updateUser
     * @return
     */
    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos, LocalDateTime createDate, String createUser, LocalDateTime updateDate, String updateUser) {
        return new ArticleDto(id, userAccountDto, title, content, hashtagDtos, createDate, createUser, updateDate, updateUser);
    }

    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content,  Set<HashtagDto> hashtagDtos) {
        return new ArticleDto(null, userAccountDto, title, content, hashtagDtos, null, null, null, null);
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
                entity.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getCreateDate(),
                entity.getCreateUser(),
                entity.getUpdateDate(),
                entity.getUpdateUser()
        );
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.of(
                userAccount,
                title,
                content
        );
    }

}