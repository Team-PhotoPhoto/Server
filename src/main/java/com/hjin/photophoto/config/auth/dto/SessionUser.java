package com.hjin.photophoto.config.auth.dto;
import com.hjin.photophoto.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // 인증된 사용자 정보만 필요
    private Long userId;
    private String emailAuth;

    public SessionUser(User user) {
        this.userId = user.getUserId();
        this.emailAuth= user.getEmailAuth();

    }
}

