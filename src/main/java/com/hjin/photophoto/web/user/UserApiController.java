package com.hjin.photophoto.web.user;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.user.UserService;
import com.hjin.photophoto.web.posts.dto.PostsResponse;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserSaveRequest;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping("/profile/signup")
    public String signUpEmail(@LoginUser SessionUser sessionUser) {
        return sessionUser.getEmailAuth();
    }

    @PutMapping ("/profile")
    public Long updateProfile(@LoginUser SessionUser user, @RequestBody UserUpdateRequest requestDto) {
        return userService.update(user.getUserId(), requestDto);
    }

    @GetMapping("/profile/me")
    public UserResponse getMyProfile (@LoginUser SessionUser sessionUser) {
        return userService.findByUserId(sessionUser.getUserId());
    }

    @GetMapping("/profile/{userId}")
    public UserResponse getOtherProfile (@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }


}
