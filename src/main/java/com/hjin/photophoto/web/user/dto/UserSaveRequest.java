package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    private String nickname;
    private int wallType;
    private int frameType;
    private String email_noti;

    @Builder
    public UserSaveRequest(String nickname, int wallType, int frameType,
                           String email_noti) {
        this.nickname = nickname;
        this.wallType = wallType;
        this.frameType = frameType;
        this.email_noti = email_noti;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .wallType(wallType)
                .frameType(frameType)
                .email_noti(email_noti)
                .build();

    }
}
