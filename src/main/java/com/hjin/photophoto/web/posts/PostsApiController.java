package com.hjin.photophoto.web.posts;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.subjects.SubjectsRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.posts.PostsService;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController     //json 변환 컨트롤러
public class PostsApiController {
    private final PostsService postsService;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @GetMapping("/post/image/subjects")
    public List<SubjectsResponse> getAllSubjects() {
        return postsService.findAllSubjects();
    }

    @PostMapping("/post/image")
    public Long uploadImage() {
        return postsService.uploadImage();
    }

    @PostMapping("/post")       //@RequestParam: request 파라미터 가져옴
    public Long savePost(@RequestBody PostsSaveRequest requestDto) {
        return postsService.save(requestDto);
    }

    @PutMapping("/post/{postId}")
    public Long updateOpen(@PathVariable Long postId, @RequestBody OpenUpdateRequest requestDto, @LoginUser SessionUser sessionUser) {
        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));

        if (requestDto.getReceiverUserId() != user.getUserId()) {
            throw new IllegalArgumentException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + user.getUserId());
        } else {
            return postsService.updateOpen(postId);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public Long deletePost(@PathVariable Long postId, @RequestParam Long receiverUserId,
                       @LoginUser SessionUser sessionUser) {
        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));

        if (receiverUserId != user.getUserId()) {
            throw new IllegalArgumentException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + user.getUserId());
        } else {
            postsService.delete(postId);
            return postId;
        }
    }

    @GetMapping("/post/{postId}")
    public PostsResponse getPost (@PathVariable Long postId) {

        postsRepository.findPostsByPostId(postId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "햬당 포스트가 없습니다. postId = " + postId
                        ));
        postsService.updateRead(postId);
        return postsService.findByPostId(postId);
    }


    @GetMapping("/inbox")
    public List<InboxResponse> getInbox(Pageable pageable, @LoginUser SessionUser sessionUser) {
        return postsService.findAllByUserId(sessionUser.getUserId(), pageable);
    }

    @GetMapping("/gallery/me")
    public List<GalleryResponse> findMyGallery(
            Pageable pageable, @LoginUser SessionUser sessionUser) {

        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));



        return postsService.findAllByUserIdOpen(user.getUserId(), pageable);
    }

    @GetMapping("/gallery/{userId}")
    public List<GalleryResponse> getOtherGallery(@PathVariable Long userId, Pageable pageable) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + userId));

        return postsService.findAllByUserIdOpen(userId, pageable);
    }



}
