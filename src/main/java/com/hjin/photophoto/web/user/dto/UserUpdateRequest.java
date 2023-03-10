package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.user.FrameType;
import com.hjin.photophoto.domain.user.Role;
import com.hjin.photophoto.domain.user.WallType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    private String nickname;
    private FrameType frameType;
    private WallType wallType;
    private String emailNoti;
    private boolean noti;
    private Role role;

    @Builder
    public UserUpdateRequest(String nickname, FrameType frameType, WallType wallType,
                             String emailNoti, boolean noti) {
        this.nickname = nickname;
        this.frameType = frameType;
        this.wallType = wallType;
        this.emailNoti = emailNoti;
        this.noti = noti;
        this.role = Role.USER;
    }
}
