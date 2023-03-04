package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.JwtTokenResponse;
import com.hjin.photophoto.config.auth.dto.MailRequest;
import com.hjin.photophoto.config.auth.dto.RefreshTokenResponse;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.postsImg.PostsImg;
import com.hjin.photophoto.domain.refreshToken.RefreshToken;
import com.hjin.photophoto.domain.refreshToken.RefreshTokenRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.web.posts.dto.PostsSaveRequest;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;


import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

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
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, userId));

        return user.getEmailAuth();

    }

    public void sendMail(MailRequest mailRequest) {

        User user = userRepository.findByUserId(Long.valueOf(mailRequest.getReceiverUserId()))
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, mailRequest.getReceiverUserId()));

        // 수신 여부 확인
        if (user.isNoti()) {
            MimeMessage message = javaMailSender.createMimeMessage();

            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                // 1. 메일 수신자, 발신자 설정
                messageHelper.setTo(user.getEmailNoti());
                messageHelper.setFrom("photophoto.official@gmail.com", "포토포토");

                // 2. 메일 제목 설정
                messageHelper.setSubject("[포토포토] 갤러리 수신함에 방명록이 도착했어!");

                // 3. 메일 내용 설정
                // HTML 적용됨
                String content = setMailBody(user.getUserId(), user.getNickname(), mailRequest.getTitle());
//                System.out.println(content);
                messageHelper.setText(content, true);

                // 4. 메일 전송
                javaMailSender.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        }



    }

    private String setMailBody (Long userId, String nickname, String title) {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "        <title>HTML Email Template</title>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"bodyTable\">\n" +
                "            <tr>\n" +
                "                <td align=\"center\">\n" +
                "                    <!-- 600px - 800px CONTENTS CONTAINER TABLE -->\n" +
                "                    <center style=\"width:100%\">\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"align-self: center; padding-top: 40px; padding-bottom: 40px; background-color: #ffffff; text-align: center\">\n" +
                "                            <tr>\n" +
                "                                <td>\n" +
                "                                    <img alt=\"\" id=\"thumb\" width=\"214px\" height=\"214px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/mailbox.png\" style=\"align-self: center;\">\n" +
                "                                    <p id=\"sender\" style=\"font-size: 20px; font-weight: 500; text-align: center; color: #989898; padding-top: 50px; padding-bottom: 16px\">"+nickname+"</p>\n" +
                "                                    <span id=\"title\" style=\"font-size: 24px; font-weight: 700; text-align: center; color: #484848;\">"+title+"</span><br>\n" +
                "                                    <p id=\"ask_title\" style=\"font-size: 16px; font-weight: 500; text-align: center; color: #535353; padding-top: 50px;\">지금 바로 확인해 볼래?</p>\n" +
                "                                    <a href=\"http://www.photophoto.me/gallery/"+userId+"\">\n" +
                "                                        <img alt=\"\" id=\"button\" width=\"286px\" height=\"52px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/btn.png\n\" style=\"align-self: center; padding-top: 8px;\">\n" +
                "                                    </a>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </center>\n" +
                "\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>\n" +
                "                    <!-- 600px - 800px CONTENTS CONTAINER TABLE -->\n" +
                "                    <center width=\"100%\">\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"padding-top: 10px; padding-bottom: 10px; background-color: #657588; text-align: center;\">\n" +
                "                                    <img alt=\"\" id=\"logo\" width=\"63px\" height=\"63px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/logo.png\" style=\"align-self: center;\">\n" +
                "                                    <p id=\"footer_content\" style=\"font-size: 14px; font-weight: 500; text-align: center; color: #ffffff; margin-top: 10px; margin-bottom: 40px;\">즐거운 추억을 친구들과 함께 공유하자!</p>\n" +
                "                                    <span id=\"footer_copyright\" style=\"font-size: 12px; font-weight: 700; text-align: center; color: #ffffff;\">© 2023 PhotoPhoto All rights reserved.</span><br>\n" +
                "                                    <span id=\"discript_block\" style=\"font-size: 9px; font-weight: 400; text-align: center; color: #ffffff;\">*메일 수신 거부는 <span style=\"text-decoration: underline;\">프로필 설정</span>에서 할 수 있어요</span>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </center>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </body>\n" +
                "</html>";
    }




}
