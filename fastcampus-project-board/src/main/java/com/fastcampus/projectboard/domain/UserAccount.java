package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true) //부모 클래스에 있는 필드들도 toString의 대상에 집어 넣겠다는 의미
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createDate"),
        @Index(columnList = "createUser")
})
@Entity
public class UserAccount extends AuditingFields {
    @Id
    @Column(length = 50)
    private String userId;

    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo, String createUser) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createUser = createUser;
        this.updateUser = createUser;
    }

    /**
     * 이미 인증된 상태 > 기존 회원 > DB에 회원 정보가 있는 경우
     * 해당 of 메소드를 사용
     * @param userId
     * @param userPassword
     * @param email
     * @param nickname
     * @param memo
     * @return
     */
    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return UserAccount.of(userId, userPassword, email, nickname, memo, null);
    }

    /**
     * 생성자 정보가 없는 경우 > ex) 회원가입하는 경우
     * 해당 of 메소드를 사용한다.
     * @param userId
     * @param userPassword
     * @param email
     * @param nickname
     * @param memo
     * @param createUser
     * @return
     */
    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo, String createUser) {
        return new UserAccount(userId, userPassword, email, nickname, memo, createUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return userId != null && userId.equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
