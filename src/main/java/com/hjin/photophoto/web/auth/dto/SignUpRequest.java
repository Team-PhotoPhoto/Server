package com.hjin.photophoto.web.auth.dto;

import com.hjin.photophoto.domain.auth.Auth;
import com.hjin.photophoto.domain.auth.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    private String email_auth;
    private String password;
    private String socialUserId;
    private Role role;

    @Builder
    public SignUpRequest(String email_auth, String password, String socialUserId
    , Role role) {
        this.email_auth = email_auth;
        this.password = password;
        this.socialUserId = socialUserId;
        this.role = role;
    }

    public Auth toEntity() {    // 처음 가입할 때 User entity 생성, GUEST 로
        return Auth.builder()
                .role(Role.USER)
                .build();
    }
}
