package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createDate,
        String email,
        String nickname,
        String userId
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createDate, String email, String nickname, String userId) {
        return new ArticleCommentResponse(id, content, createDate, email, nickname, userId);
    }

    /**
     * ArticleCommentRequest의 toDto를 사용해서
     * 세팅된 ArticleCommentResponse객체를 화면에 전달할 때 사용하는 함수인 듯
     * @param dto
     * @return
     */
    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createDate(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId()
        );
    }

}