package com.hjin.photophoto.config.auth.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class JwtTokenResponse {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
