package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        LocalDateTime createUser,
        String createdBy,
        LocalDateTime updateDate,
        String updateUser
) {

    public static UserAccountDto of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(String userId, String userPassword, String email, String nickname, String memo, LocalDateTime createdAt, String createUser, LocalDateTime updateDate, String updateUser) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, createdAt, createUser, updateDate, updateUser);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreateDate(),
                entity.getCreateUser(),
                entity.getUpdateDate(),
                entity.getUpdateUser()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                userId,
                userPassword,
                email,
                nickname,
                memo
        );
    }

}