package com.hjin.photophoto.web.posts;

import com.hjin.photophoto.config.auth.AuthService;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.service.posts.PostsService;
import com.hjin.photophoto.service.view.ViewService;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController     //json 변환 컨트롤러
public class PostsApiController {
    private final PostsService postsService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ViewService viewService;

    @GetMapping("/api/post/image/subjects")
    public List<SubjectsResponse> getAllSubjects() {
        return postsService.findAllSubjects();
    }

    @PostMapping("/api/post/image")
    public Long uploadImage() {
        return postsService.uploadImage();
    }

    @GetMapping("/api/post/image/{postId}")
    public String getPostsUploadUrl(@PathVariable Long postId, @RequestParam String type) {
        return postsService.getPostUploadUrl(postId, type);
    }

    @PostMapping("/api/post")       //@RequestParam: request 파라미터 가져옴
    public Long savePost(@RequestBody PostsSaveRequest requestDto) {
        // unreadCount++
        viewService.plus1ViewCount(requestDto.getReceiverUserId());

        // save post
        postsService.save(requestDto);

        // send email
        postsService.sendMail(requestDto);

        return requestDto.getPostId();
    }

    @PostMapping("/api/post/me")       //@RequestParam: request 파라미터 가져옴
    public Long saveMyPost(@RequestBody PostsSaveRequest requestDto, HttpServletRequest request) {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        // 나->나인가?
        if (!Objects.equals(requestDto.getReceiverUserId(), userIdFromHeader)) {
            throw new MyException(MyExceptionType.NO_PERMISSION, userIdFromHeader);
        } else {
            return postsService.save(requestDto);
        }

    }

    @PutMapping("/api/post/{postId}")
    public Long updateOpen(@PathVariable Long postId, HttpServletRequest request) {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);
        return postsService.updateOpen(postId, userIdFromHeader);
    }

    @DeleteMapping("/api/post/{postId}")
    public Long deletePost(@PathVariable Long postId, HttpServletRequest request) {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);
        postsService.delete(postId, userIdFromHeader);
        return postId;
    }

    @GetMapping("/api/post/{postId}")
    public PostsResponse getPost(@PathVariable Long postId) {
        PostsResponse postsResponse = postsService.findByPostId(postId);

        if (!postsResponse.isOpenYn()) {
            // open 되지 않은 포스트 읽기 금지
            throw new MyException(MyExceptionType.NO_PERMISSION, postId);
        }
        return postsService.findByPostId(postId);
    }


    @GetMapping("/api/inbox")
    public List<InboxResponse> getInbox(Pageable pageable, HttpServletRequest request) {

        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        User user = userRepository.findByUserId(userIdFromHeader)
                .orElseThrow(() -> new MyException(MyExceptionType.NO_PERMISSION, userIdFromHeader));

        return postsService.findAllByUserId(user.getUserId(), pageable);
    }

    @GetMapping("/api/inbox/post/{postId}")
    public PostsResponse getInboxPost(@PathVariable Long postId, HttpServletRequest request) {
        postsService.updateRead(postId);

        // unreadCount--
        Long userIdFromHeader = authService.getUserIdFromHeader(request);
        viewService.minus1ViewCount(userIdFromHeader);

        return postsService.findByPostId(postId);
    }

    @GetMapping("/api/gallery/me")
    public List<GalleryResponse> findMyGallery(
            Pageable pageable, HttpServletRequest request) {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        User user = userRepository.findByUserId(userIdFromHeader)
                .orElseThrow(() -> new MyException(MyExceptionType.NO_PERMISSION, userIdFromHeader));

        return postsService.findAllByUserIdOpen(user.getUserId(), pageable);
    }

    @GetMapping("/api/gallery/{userId}")
    public List<GalleryResponse> getOtherGallery(@PathVariable Long userId, Pageable pageable) {
        return postsService.findAllByUserIdOpen(userId, pageable);
    }


}
