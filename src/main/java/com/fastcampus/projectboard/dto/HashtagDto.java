package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Hashtag;

import java.time.LocalDateTime;
import java.util.Set;

public record HashtagDto(
        Long id,
        String hashtagName,
        LocalDateTime createDate,
        String createUser,
        LocalDateTime updateDate,
        String updateUser
) {
    public static HashtagDto of(String hashtagName) {
        return new HashtagDto(null, hashtagName, null, null, null, null);
    }

    public static HashtagDto of(Long id, String hashtagName, LocalDateTime createDate, String createUser, LocalDateTime updateDate, String updateUser) {
        return new HashtagDto(id, hashtagName, createDate, createUser, updateDate, updateUser);
    }

    public static HashtagDto from(Hashtag entity) {
        return HashtagDto.of(
                entity.getId(),
                entity.getHashtagName(),
                entity.getCreateDate(),
                entity.getCreateUser(),
                entity.getUpdateDate(),
                entity.getUpdateUser()
        );
    }

    public Hashtag toEntity(){
        return Hashtag.of(hashtagName);
    }
}
