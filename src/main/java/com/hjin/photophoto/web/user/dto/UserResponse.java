package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.user.FrameType;
import com.hjin.photophoto.domain.user.User;

public class UserResponse {
    private Long userId;
    private String imageUrl;
    private String nickname;
    private FrameType frameType;
    private String emailNoti;
    private boolean noti;

    public UserResponse(User entity) {
        this.userId = entity.getUserId();
        this.imageUrl = entity.getImageUrl();
        this.nickname = entity.getNickname();
        this.frameType = entity.getFrameType();
        this.emailNoti = entity.getEmailNoti();
        this.noti = entity.isNoti();
    }
}
