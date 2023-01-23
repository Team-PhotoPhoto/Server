package com.hjin.photophoto.config.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {
    String accessToken;
}
