package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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
}
