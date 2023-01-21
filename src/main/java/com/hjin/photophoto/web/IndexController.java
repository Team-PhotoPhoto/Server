package com.hjin.photophoto.web;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
import com.hjin.photophoto.domain.user.FrameType;
import com.hjin.photophoto.domain.user.Role;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final UserRepository userRepository;


    // 메인화면
    @GetMapping("/")
    public String index(@LoginUser SessionUser sessionUser){ // @LoginUser 만 사용하면 세션 정보 가져올 수 있다

        //로그인 됨
//        if (sessionUser != null) {
//
//            User user = userRepository.findByUserId(sessionUser.getUserId())
//                    .orElseThrow(() -> new IllegalArgumentException
//                            ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));
//
//            if(user.getRole().getKey() == Role.JOIN.getKey()) {
//                // 회원가입 진행 중 유저
//                return "redirect:/profile/signup";
//            }
//            return "redirect:/gallery/me";
//        }

        // 로그인 x 유저
        return "index";
    }


//    @GetMapping("/gallery/me")
//    public String galleryMe(Model model, @LoginUser SessionUser sessionUser, Pageable pageable) {
//
//
//        User user = userRepository.findByUserId(sessionUser.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException
//                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));
//
//        if(user.getRole().getKey() == Role.JOIN.getKey()) {
//            // 회원가입 진행 중 유저
//            return "redirect:/profile/signup";
//        }
//        if(user.getRole().getKey() == Role.USER.getKey()) {
//            // 회원가입 완료 유저
//            model.addAttribute("nickname", user.getNickname());
//            String imageUrl = "http://localhost:8080/gallery/" + user.getUserId();
//            model.addAttribute("galleryUrl", imageUrl);
//
//            model.addAttribute("posts", postsService.findAllByUserIdOpen(user.getUserId(), pageable));
//
//            return "gallery-me";
//        }
//        return "index";
//    }
//
//
//    @GetMapping("/profile/me")
//    public String profileMe(Model model, @LoginUser SessionUser sessionUser) {
//
//        User user = userRepository.findByUserId(sessionUser.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException
//                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));
//
//
//        model.addAttribute("imageUrl", user.getImageUrl());
//        model.addAttribute("nickname", user.getNickname());
//        model.addAttribute("wallType", user.getWallType());
//        model.addAttribute("frameType", user.getFrameType());
//        model.addAttribute("noti", user.isNoti());
//        model.addAttribute("emailNoti", user.getEmailNoti());
//
//        return "profile-me";
//    }
//
//
//    @GetMapping("/posts/image")
//    public String postsImageUpload(Model model) {
////        model.addAttribute("subjects", subjectsService.findAll());
//
////        return "inbox";
//        return "image-upload";
//    }


}
