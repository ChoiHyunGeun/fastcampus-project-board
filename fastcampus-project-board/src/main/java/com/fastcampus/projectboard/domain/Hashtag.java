package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(
    indexes = {
            @Index(columnList = "hashtagName", unique = true),
            @Index(columnList = "createDate"),
            @Index(columnList = "createUser")
    }
)
@Entity
public class Hashtag extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 증가시키겠다는 말
    Long id;

    /**
     * ManyToMany나 OneToMany같은 어노테이션 사용할 때
     * 서로 꼬리를 물고 계속 toSring을 호출되는 문제 주의하기
     * 그래서 @ToString.Exclude 어노테이션 선언
     */
    @ToString.Exclude
    @ManyToMany(mappedBy = "hashtags")
    private Set<Article> articles = new LinkedHashSet<>();

    @Setter @Column(nullable = false) private String hashtagName;

    protected Hashtag() {}

    protected Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public static Hashtag of(String hashtagName) {
        return new Hashtag(hashtagName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return this.getId() != null && Objects.equals(getId(), hashtag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
