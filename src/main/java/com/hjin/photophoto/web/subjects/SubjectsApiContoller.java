package com.hjin.photophoto.web.subjects;

import com.hjin.photophoto.service.subjects.SubjectsService;
import com.hjin.photophoto.web.posts.dto.GalleryResponse;
import com.hjin.photophoto.web.subjects.dto.SubjectsRespoonse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController     //json 변환 컨트롤러
public class SubjectsApiContoller {
    private final SubjectsService subjectsService;

//    @GetMapping("/posts/image")
//    public List<SubjectsRespoonse> findAll(Model model) {
//        return subjectsService.findAll();
//
//        model.addAttribute("posts", postsService.findAllByUserId(user.getUserId(), pageable));
//
//        return "inbox";
//    }

//    @GetMapping("/posts/image")
//    public String findAll(Model model) {
//
//        model.addAttribute("subjects", subjectsService.findAll());
//
//        return "inbox";
//    }
}
