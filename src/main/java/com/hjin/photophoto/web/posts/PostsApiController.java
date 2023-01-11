package com.hjin.photophoto.web.posts;

import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.service.posts.PostsService;
import com.hjin.photophoto.web.posts.dto.GalleryResponse;
import com.hjin.photophoto.web.posts.dto.InboxResponse;
import com.hjin.photophoto.web.posts.dto.PostsResponse;
import com.hjin.photophoto.web.posts.dto.PostsSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController     //json 변환 컨트롤러
public class PostsApiController {
    private final PostsService postsService;

    @PostMapping("/post")       //@RequestParam: request 파라미터 가져옴
    public Long save(@RequestBody PostsSaveRequest requestDto) {
        return postsService.save(requestDto);
    }

    @PatchMapping("/post/{postId}")
    public Long update(@PathVariable Long postId) {
        return postsService.updateOpen(postId);
    }

    @GetMapping("/post/{postId}")
    public PostsResponse findByPostId (@PathVariable Long postId) {
        postsService.updateRead(postId);
        return postsService.findByPostId(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public Long delete(@PathVariable Long postId) {
        postsService.delete(postId);
        return postId;
    }

    @GetMapping("/inbox/{userId}")
    public List<InboxResponse> findAllByUserId(@PathVariable Long userId) {
        return postsService.findAllByUserId(userId);
    }

    @GetMapping("/gallery/{userId}")
    public List<GalleryResponse> findAllByUserIdOpen(@PathVariable Long userId) {
        return postsService.findAllByUserIdOpen(userId);
    }


}
