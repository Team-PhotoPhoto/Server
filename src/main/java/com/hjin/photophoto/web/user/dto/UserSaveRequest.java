package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.user.FrameType;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.WallType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    private String nickname;
    private FrameType frameType;
    private WallType wallType;
    private String emailNoti;

    @Builder
    public UserSaveRequest(String nickname, FrameType frameType, String emailNoti) {
        this.nickname = nickname;
        this.frameType = frameType;
        this.emailNoti = emailNoti;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .frameType(frameType)
                .wallType(wallType)
                .emailNoti(emailNoti)
                .build();

    }
}
