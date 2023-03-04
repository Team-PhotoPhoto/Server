package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.MailRequest;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
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
    public void sendMail(@RequestBody MailRequest requestDto) {
        authService.sendMail(requestDto);
    }
}
