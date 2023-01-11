package com.hjin.photophoto.config2.auth.dto;
import com.hjin.photophoto.domain.auth.Auth;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // 인증된 사용자 정보만 필요
    private String email_auth;

    public SessionUser(Auth auth) {
        this.email_auth = auth.getEmail_auth();
    }
}

