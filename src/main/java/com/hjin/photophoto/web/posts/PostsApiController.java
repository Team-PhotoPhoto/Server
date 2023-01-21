package com.hjin.photophoto.web.posts;

import com.hjin.photophoto.config.auth.LoginUser;
import com.hjin.photophoto.config.auth.dto.SessionUser;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.posts.PostsService;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "http://ec2-54-180-92-252.ap-northeast-2.compute.amazonaws.com:8080")
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
    public Long updateOpen(@PathVariable Long postId, @RequestBody OpenUpdateRequest requestDto
            , @LoginUser SessionUser sessionUser) throws AccessDeniedException {

        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));

        if (!Objects.equals(requestDto.getReceiverUserId(), user.getUserId())) {
            throw new AccessDeniedException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + user.getUserId());
        } else {
            return postsService.updateOpen(postId);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public Long deletePost(@PathVariable Long postId, @RequestParam Long receiverUserId,
                           @LoginUser SessionUser sessionUser) throws AccessDeniedException {
        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + sessionUser.getUserId()));

        // Long: 2가지 타입(Long, long) null 일 수 있음 -> null pointer 예외
        if (!Objects.equals(receiverUserId, user.getUserId())) {
            throw new AccessDeniedException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + user.getUserId());
        } else {
            postsService.delete(postId);
            return postId;
        }
    }

    @GetMapping("/post/{postId}")
    public PostsResponse getPost(@PathVariable Long postId) {
        postsService.updateRead(postId);
        return postsService.findByPostId(postId);
    }


    @GetMapping("/inbox")
    public List<InboxResponse> getInbox(Pageable pageable, @LoginUser SessionUser sessionUser) throws AccessDeniedException {

        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + sessionUser.getUserId()));


        return postsService.findAllByUserId(user.getUserId(), pageable);
    }

    @GetMapping("/gallery/me")
    public List<GalleryResponse> findMyGallery(
            Pageable pageable, @LoginUser SessionUser sessionUser) throws AccessDeniedException {

        User user = userRepository.findByUserId(sessionUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + sessionUser.getUserId()));

        return postsService.findAllByUserIdOpen(user.getUserId(), pageable);
    }

    @GetMapping("/gallery/{userId}")
    public List<GalleryResponse> getOtherGallery(@PathVariable Long userId, Pageable pageable) {
        return postsService.findAllByUserIdOpen(userId, pageable);
    }


}
