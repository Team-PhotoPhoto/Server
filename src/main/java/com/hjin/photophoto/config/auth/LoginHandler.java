package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.JwtTokenResponse;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.refreshToken.RefreshToken;
import com.hjin.photophoto.domain.refreshToken.RefreshTokenRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Component
public class LoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtUtil jwtUtil;

    @Value("${client.host}")
    private String CLIENT_HOST;


    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthService authService;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess (HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException {

        //authentication: 인증 토큰

        //login 성공한 사용자 목록
        //oAuth2User.toString() 예시
        //: Name: [2346930276], Granted Authorities: [[JOIN]], User Attributes: [{id=?, provider=kakao, email=?}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        Long userId = userRepository.findByEmailAuth(email)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. emailAuth = " + email))
                .getUserId();

        System.out.println("email: " + email);


        // 토큰 발행
        JwtTokenResponse jwtToken = jwtUtil.generateToken(userId);

        // 리프레시 토큰 저장
        authService.saveRefreshToken(userId, jwtToken);

        // refreshToken DB 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                        .key(userId)
                                        .token(jwtToken.getRefreshToken())
                                        .build();
        refreshTokenRepository.save(refreshToken);


        // 인증 코드를 담은 리다이렉트 uri 생성
        String url = makeRedirectUrl(jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        if (response.isCommitted()) {
            logger.debug("응답이 이미 커밋된 상태입니다. " + url + "로 리다이렉트하도록 바꿀 수 없습니다.");
            return;
        }
        System.out.println("url: " + url);
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(CLIENT_HOST)
                .port(3000)
                .path("/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }




}
