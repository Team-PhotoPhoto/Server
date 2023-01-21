package com.hjin.photophoto.web.user;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.user.UserService;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@CrossOrigin(origins = "http://ec2-54-180-92-252.ap-northeast-2.compute.amazonaws.com:8080")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile/signup")
    public String signUpEmail(@LoginUser SessionUser sessionUser) {
        return sessionUser.getEmailAuth();
    }

    @PutMapping("/profile")
    public Long updateProfile(@LoginUser SessionUser user, @RequestBody UserUpdateRequest requestDto) {
        return userService.update(user.getUserId(), requestDto);
    }

    @GetMapping("/profile/me")
    public UserResponse getMyProfile(@LoginUser SessionUser sessionUser) throws AccessDeniedException {

        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + sessionUser.getUserId()));

        return userService.findByUserId(user.getUserId());
    }

    @GetMapping("/profile/{userId}")
    public UserResponse getOtherProfile(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PutMapping("/deleteaccount/{userId}")
    public void deleteAccount(@PathVariable Long userId, @LoginUser SessionUser sessionUser) throws AccessDeniedException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + userId));

        if(!Objects.equals(userId, user.getUserId())) {
            throw new AccessDeniedException("해당 계정을 탈퇴할 수 있는 유저가 아닙니다. userId = " + user.getUserId());
        }
        else {
            userService.updateDeleteByUserId(userId);
        }
    }


}
