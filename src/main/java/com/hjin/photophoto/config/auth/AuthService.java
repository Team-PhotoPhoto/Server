package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.JwtTokenResponse;
import com.hjin.photophoto.config.auth.dto.RefreshTokenResponse;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.postsImg.PostsImg;
import com.hjin.photophoto.domain.refreshToken.RefreshToken;
import com.hjin.photophoto.domain.refreshToken.RefreshTokenRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.web.posts.dto.PostsSaveRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Transactional
    public void saveRefreshToken(Long userId, JwtTokenResponse jwtTokenResponse) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(userId)
                .token(jwtTokenResponse.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    //access, refresh 둘 다 발행
//    public JwtTokenResponse reissue(String accessToken, String refreshToken) throws AccessDeniedException {
////        if (!jwtUtil.isValidToken(refreshToken)) {
////            throw new AccessDeniedException("접근 권한이 없습니다.");
////        }
//
//        Claims claims = jwtUtil.getAllClaims(accessToken.substring(7));
//        Long userId = Long.valueOf(claims.get("userId").toString());
//
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new AccessDeniedException
//                        ("접근 권한이 없습니다. userId = " + claims.get("userId")));
//        RefreshToken refreshTokenUpdate = refreshTokenRepository.findByKey(userId)
//                .orElseThrow(() -> new AccessDeniedException
//                        ("접근 권한이 없습니다. userId = " + claims.get("userId")));
//
//        if (!refreshTokenUpdate.getToken().equals((refreshToken))) {
//            throw  new AccessDeniedException("접근 권한이 없습니다.");
//        }
//
//        JwtTokenResponse newTokenResponse = jwtUtil.generateToken(userId);
//        refreshTokenUpdate.updateToken(newTokenResponse.getRefreshToken());
//
//        return newTokenResponse;
//    }

    //access 만 발행
    public String refresh(String refreshToken) throws AccessDeniedException {
        // refresh token 만료 검사
        if (!jwtUtil.isValidToken(refreshToken)) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        // DB에서 refreshToken 찾기
        RefreshToken savedRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. refreshToken: " + refreshToken));
        User user = userRepository.findByUserId(savedRefreshToken.getKey())
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. key: " + savedRefreshToken.getKey()));

        if (!refreshToken.equals(savedRefreshToken.getToken())) {
            throw new AccessDeniedException(
                    ("접근 권한이 없습니다. key: " + savedRefreshToken.getKey()));
        }

        // access 발행
        JwtTokenResponse newToken = jwtUtil.generateToken(user.getUserId());

        return newToken.getRefreshToken();
    }

    /* 토큰을 헤더에 배치 */
//    public HttpHeaders setTokenHeaders(JwtTokenResponse tokenDto) {
//        HttpHeaders headers = new HttpHeaders();
//        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
//                .path("/")
//                .maxAge(60*60*24*14) // 쿠키 유효기간 14일로 설정했음
//                .secure(true)
//                .sameSite("None")
//                .httpOnly(true)
//                .build();
//        headers.add("Set-cookie", cookie.toString());
//        headers.add("Authorization", tokenDto.getAccessToken());
//
//        return headers;
//    }

    public Long getUserIdFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        Claims claims = jwtUtil.getAllClaims(accessToken.substring(7));
        Long userId = Long.valueOf(claims.get("userId").toString());

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("존재하지 않는 유저입니다. userId = " + userId));

        return user.getUserId();

    }

    public String getEmailAuthFromHeader(HttpServletRequest request) {


        String accessToken = request.getHeader("Authorization");
        Claims claims = jwtUtil.getAllClaims(accessToken.substring(7));

        Long userId = Long.valueOf(claims.get("userId").toString());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("존재하지 않는 유저입니다. userId = " + userId));

        return user.getEmailAuth();

    }




}
