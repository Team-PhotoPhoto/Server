package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

//    @GetMapping("/reissue")
//    public JwtTokenResponse reissue(HttpServletRequest request, @CookieValue(name = "RefreshToken") Cookie cookie)
//            throws AccessDeniedException {
//        String accessToken = request.getHeader("Authorization");
//        String refreshToken = cookie.getValue();
//
//        return authService.reissue(accessToken, refreshToken);
//    }

    @GetMapping("/refresh")
    public String refresh(@RequestParam String refreshToken)
            throws AccessDeniedException {
        return authService.refresh(refreshToken);
    }


    @PostMapping("/mail")
    public void sendMail(@RequestParam String userId) {
        User user = userRepository.findByUserId(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 유저가 없습니다. userId = " + userId
                ));

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // 1. 메일 수신자 설정
            messageHelper.setTo(user.getEmailNoti());

            // 2. 메일 제목 설정
            messageHelper.setSubject("test title");

            // 3. 메일 내용 설정
            // HTML 적용됨
            String content = "테스트. <b>테스트</b>";
            messageHelper.setText(content, true);

            // 4. 메일 전송
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
