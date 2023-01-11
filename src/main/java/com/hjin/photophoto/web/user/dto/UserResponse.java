package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.user.User;
import lombok.Builder;

public class UserResponse {
    private Long userId;
    private String imageUrl;
    private String nickname;
    private int wallType;
    private int frameType;
    private String email_noti;
    private boolean noti;

    public UserResponse(User entity) {
        this.userId = entity.getUserId();
        this.imageUrl = entity.getImageUrl();
        this.nickname = entity.getNickname();
        this.wallType = entity.getWallType();
        this.frameType = entity.getFrameType();
        this.email_noti = entity.getEmail_noti();
        this.noti = entity.isNoti();
    }
}
