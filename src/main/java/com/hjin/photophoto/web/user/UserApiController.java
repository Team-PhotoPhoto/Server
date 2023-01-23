package com.hjin.photophoto.web.user;

import com.hjin.photophoto.config.auth.AuthService;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.user.UserService;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @GetMapping("/profile/signup")
    public String signUpEmail(HttpServletRequest request) {
        String emailAuth = authService.getEmailAuthFromHeader(request);
        System.out.println("emailAuth: " + emailAuth);
        return emailAuth;
    }

    @PutMapping("/profile")
    public Long updateProfile(HttpServletRequest request, @RequestBody UserUpdateRequest requestDto) {
        Long userId = authService.getUserIdFromHeader(request);
        return userService.update(userId, requestDto);
    }

    @GetMapping("/profile/me")
    public UserResponse getMyProfile(HttpServletRequest request) throws AccessDeniedException {
        Long userId = authService.getUserIdFromHeader(request);
        System.out.println(">>>>>> profile.me: " + userId);

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + userId));

        return userService.findByUserId(userId);
    }

    @GetMapping("/profile/{userId}")
    public UserResponse getOtherProfile(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PutMapping("/deleteaccount/{userId}")
    public void deleteAccount(@PathVariable Long userId, HttpServletRequest request) throws AccessDeniedException {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + userId));

        if(!Objects.equals(userId, userIdFromHeader)) {
            throw new AccessDeniedException("해당 계정을 탈퇴할 수 있는 유저가 아닙니다. userId = " + user.getUserId());
        }
        else {
            userService.updateDeleteByUserId(userId);
        }
    }


}
