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

    @Column(length = 30, name = "nickname")
    private String nickname;

    @Column(length = 200, name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "frameType")
    private FrameType frameType;


    @Enumerated(EnumType.STRING)
    @Column(name = "wallType")
    private WallType wallType;

//    @Column(columnDefinition = "TINYINT(2)", nullable = false, name = "frame_type")
//    private int frameType;

    @Column(length = 100, nullable = false, name = "emailAuth")
    private String emailAuth;
    @Column(length = 100, name = "emailNoti")
    private String emailNoti;

    @Column(name = "noti")
    private boolean noti;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Builder        // 생성자
    public User(Long userId, String nickname, String imageUrl,
                FrameType frameType, WallType wallType, String  emailAuth, String emailNoti, boolean noti, Role role) {
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.frameType = frameType;
        this.wallType = wallType;
        this.emailAuth = emailAuth;
        this.emailNoti = emailNoti;
        this.noti = noti;
        this.role = role;
    }

    public void update(String imageUrl, String nickname, FrameType frameType, WallType wallType,String emailNoti, boolean noti) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.frameType = frameType;
        this.wallType = wallType;
        this.emailNoti = emailNoti;
        this.noti = noti;
        this.role = Role.USER;
    }

    public void updateDelete() {
        this.role = Role.DELETED;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}

