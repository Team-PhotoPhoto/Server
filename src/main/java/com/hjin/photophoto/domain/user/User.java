package com.hjin.photophoto.domain.user;


import com.hjin.photophoto.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id     //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //PK 생성 규칙, auto increment
    private Long userId;

    @Column(length = 200, nullable = false)
    private String socialUserId;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "TINYINT(2)", nullable = false)
    private int wallType;

    @Column(columnDefinition = "TINYINT(2)", nullable = false)
    private int frameType;

    @Column(length = 100, nullable = false)
    private String email_auth;

    @Column(length = 100, nullable = false)
    private String email_noti;

    @Column(nullable = false)
    private boolean isNoti;


    @Builder        // 생성자
    public User(Long userId, String socialUserId, String nickname, String imageUrl,
                 int wallType, int frameType,
                 String email_auth, String email_noti, boolean isNoti) {
        this.userId = userId;
        this.socialUserId = socialUserId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.wallType = wallType;
        this.frameType = frameType;
        this.email_auth = email_auth;
        this.email_noti = email_noti;
        this.isNoti = isNoti;
    }
}

