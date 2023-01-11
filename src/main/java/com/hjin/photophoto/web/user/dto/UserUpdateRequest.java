package com.hjin.photophoto.web.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    private String imageUrl;
    private String nickname;
    private int wallType;
    private int frameType;
    private String email_noti;
    private boolean noti;

    @Builder
    public UserUpdateRequest(String imageUrl, String nickname, int wallType,
                             int frameType, String email_noti, boolean noti) {
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.wallType = wallType;
        this.frameType = frameType;
        this.email_noti = email_noti;
        this.noti = noti;
    }
}
