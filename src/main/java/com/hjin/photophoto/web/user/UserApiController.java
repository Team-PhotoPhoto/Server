package com.hjin.photophoto.web.user;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
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


//    @PostMapping("/profile")       //@RequestParam: request 파라미터 가져옴
//    public Long save(@RequestBody UserSaveRequest requestDto) {
//        return userService.save(requestDto);
//    }

    @PutMapping ("/profile")
    public Long update(@LoginUser SessionUser user, @RequestBody UserUpdateRequest requestDto) {
        return userService.update(user.getUserId(), requestDto);
    }

    @GetMapping("/profile/{userId}")
    public UserResponse findByPostId (@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }


}
