package com.fastcampus.projectboard.dto.response;

import com.fastcampus.projectboard.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createDate,
        String email,
        String nickname,
        String userId,
        Long parentCommentId,
        Set<ArticleCommentResponse> childComments
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createDate, String email, String nickname, String userId) {
        return ArticleCommentResponse.of(id, content, createDate, email, nickname, userId, null);
    }

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createDate, String email, String nickname, String userId, Long parentCommentId) {
        //TreeSet이 어떤 기준으로 정렬이 될 지 rule을 정하는 거임 > 실제로 TreeSet에 대댓글이 세팅되면 해당 rule로 대댓글이 정렬된다.
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
                //createDate를 기준으로 정렬하고 역순으로 정렬하려면 .reversed()를 사용하면 됨.
                .comparing(ArticleCommentResponse::createDate)
                //위에서 한번 정렬을 했지만 혹시 동일한 시간에 댓글이 작성될 경우를 생각하여 id로 한번 더 정렬해 줌
                .thenComparingLong(ArticleCommentResponse::id);
        return new ArticleCommentResponse(id, content, createDate, email, nickname, userId, parentCommentId, new TreeSet<>(childCommentComparator));
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

        return ArticleCommentResponse.of(
                dto.id(),
                dto.content(),
                dto.createDate(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                dto.parentCommentId()
        );
    }

    /**
     * 대댓글을 갖고 있는 댓글인지 체크
     * @return
     */
    public boolean hasParentComment() {
        return parentCommentId != null;
    }
}