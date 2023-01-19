package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.user.FrameType;
import com.hjin.photophoto.domain.user.Role;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.WallType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter     //setter 생성x
@NoArgsConstructor      //생성자 생성 -> Bean 주입
public class UserResponse {
    private Long userId;
    private String imageUrl;
    private String nickname;
    private WallType wallType;
    private FrameType frameType;
    private String emailNoti;
    private boolean noti;
    private Role role;

    public UserResponse(User entity) {
        this.userId = entity.getUserId();
        this.imageUrl = entity.getImageUrl();
        this.nickname = entity.getNickname();
        this.wallType = entity.getWallType();
        this.frameType = entity.getFrameType();
        this.emailNoti = entity.getEmailNoti();
        this.noti = entity.isNoti();
        this.role = entity.getRole();
    }
}
