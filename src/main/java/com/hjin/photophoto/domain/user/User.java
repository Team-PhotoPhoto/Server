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
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false, name = "auth_id")
    private Long authId;

    @Column(length = 30, nullable = false, name = "nickname")
    private String nickname;

    @Column(length = 200, nullable = false, name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "TINYINT(2)", nullable = false, name = "wall_type")
    private int wallType;

    @Column(columnDefinition = "TINYINT(2)", nullable = false, name = "frame_type")
    private int frameType;

    @Column(length = 100, nullable = false, name = "email_noti")
    private String email_noti;

    @Column(nullable = false, name = "noti")
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

