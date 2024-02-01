package com.fastcampus.projectboard.dto.request;

import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.HashtagDto;
import com.fastcampus.projectboard.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {

    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    /**
     * 해시태그가 없을 때 사용하는 toDto인 듯
     * @param userAccountDto
     * @return
     */
    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return toDto(userAccountDto, null);
    }

    /**
     * 해시태그가 있는 게시글일 때 사용하는 toDto인 듯
     * @param userAccountDto
     * @param hashtagDtos
     * @return
     */
    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtagDtos
        );
    }

}