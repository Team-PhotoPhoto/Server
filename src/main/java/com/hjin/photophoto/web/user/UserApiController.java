package com.hjin.photophoto.web.user;

import com.hjin.photophoto.config.auth.AuthService;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.service.user.UserService;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;
//    private final ViewService viewService;

    @GetMapping("/api/profile/signup")
    public String signUpEmail(HttpServletRequest request) {
        String emailAuth = authService.getEmailAuthFromHeader(request);
        // unreadCount = 0
//        Long userId = authService.getUserIdFromHeader(request);
//        viewService.saveView(userId);

//        System.out.println("emailAuth: " + emailAuth);
        return emailAuth;
    }

    @PutMapping("/api/profile")
    public Long updateProfile(HttpServletRequest request, @RequestBody UserUpdateRequest requestDto) {
        Long userId = authService.getUserIdFromHeader(request);
        return userService.update(userId, requestDto);
    }

    @GetMapping("/api/profile/me")
    public UserResponse getMyProfile(HttpServletRequest request) {
        Long userId = authService.getUserIdFromHeader(request);
//        System.out.println(">>>>>> profile.me: " + userId);
        return userService.findByUserId(userId);
    }

    @GetMapping("/api/profile/me/image")
    public String getProfileUploadUrl(HttpServletRequest request) {
        Long userId = authService.getUserIdFromHeader(request);
        return userService.getUserUploadUrl(userId);
    }

    @GetMapping("/api/profile/{userId}")
    public UserResponse getOtherProfile(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @DeleteMapping("/api/profile/me")
    public void deleteAccount(HttpServletRequest request) {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);
        userService.updateDeleteByUserId(userIdFromHeader);
    }

}
