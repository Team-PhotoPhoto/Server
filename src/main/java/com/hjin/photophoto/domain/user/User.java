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

    @Column(unique = true, nullable = false)
    private Long authId;

    @Column(length = 30, nullable = false)
    private String nickname;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "TINYINT(2)", nullable = false)
    private int wallType;

    @Column(columnDefinition = "TINYINT(2)", nullable = false)
    private int frameType;

    @Column(length = 100, nullable = false)
    private String email_noti;

    @Column(nullable = false)
    private boolean noti;

    @Builder        // 생성자
    public User(Long userId, Long authId, String nickname, String imageUrl,
                 int wallType, int frameType,  String email_noti,
                boolean noti) {
        this.userId = userId;
        this.authId = authId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.wallType = wallType;
        this.frameType = frameType;
        this.email_noti = email_noti;
        this.noti = noti;
    }

    public void update(String imageUrl, String nickname, int wallType,
                       int frameType,String email_noti, boolean noti) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.wallType = wallType;
        this.frameType = frameType;
        this.email_noti = email_noti;
        this.noti = noti;
    }
}

