package com.hjin.photophoto.web.posts;

import com.hjin.photophoto.config.auth.AuthService;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.service.postImg.PostsImgService;
import com.hjin.photophoto.service.posts.PostsService;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController     //json 변환 컨트롤러
public class PostsApiController {
    private final PostsService postsService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostsImgService postsImgService;

    @GetMapping("/post/image/subjects")
    public List<SubjectsResponse> getAllSubjects() {
        return postsService.findAllSubjects();
    }

    @PostMapping("/post/image")
    public Long uploadImage() {
        return postsService.uploadImage();
    }

    @GetMapping("/post/image/{postId}")
    public String getUploadUrl(@PathVariable Long postId, @RequestParam String folder) {
        return postsImgService.signBucket(folder, postId);
    }

    @PostMapping("/post")       //@RequestParam: request 파라미터 가져옴
    public Long savePost(@RequestBody PostsSaveRequest requestDto) {
        return postsService.save(requestDto);
    }

    @PostMapping("/post/me")       //@RequestParam: request 파라미터 가져옴
    public Long saveMyPost(@RequestBody PostsSaveRequest requestDto, HttpServletRequest request) throws AccessDeniedException {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        // 나->나인가?
        if (!Objects.equals(requestDto.getReceiverUserId(), userIdFromHeader)) {
            throw new AccessDeniedException("해당 게시글을 저장할 수 있는 유저가 아닙니다. postId = " + userIdFromHeader);
        } else {
            return postsService.save(requestDto);
        }

    }

    @PutMapping("/post/{postId}")
    public Long updateOpen(@PathVariable Long postId, @RequestBody OpenUpdateRequest requestDto
            , HttpServletRequest request) throws AccessDeniedException {

        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        if (!Objects.equals(requestDto.getReceiverUserId(), userIdFromHeader)) {
            throw new AccessDeniedException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + userIdFromHeader);
        } else {
            return postsService.updateOpen(postId);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public Long deletePost(@PathVariable Long postId, HttpServletRequest request) throws AccessDeniedException {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);
        postsService.delete(postId, userIdFromHeader);
        return postId;
    }

    @GetMapping("/post/{postId}")
    public PostsResponse getPost(@PathVariable Long postId) {
        postsService.updateRead(postId);
        return postsService.findByPostId(postId);
    }


    @GetMapping("/inbox")
    public List<InboxResponse> getInbox(Pageable pageable, HttpServletRequest request) throws AccessDeniedException {

        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        User user = userRepository.findByUserId(userIdFromHeader)
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + userIdFromHeader));

        return postsService.findAllByUserId(user.getUserId(), pageable);
    }

    @GetMapping("/gallery/me")
    public List<GalleryResponse> findMyGallery(
            Pageable pageable, HttpServletRequest request) throws AccessDeniedException {
        Long userIdFromHeader = authService.getUserIdFromHeader(request);

        User user = userRepository.findByUserId(userIdFromHeader)
                .orElseThrow(() -> new AccessDeniedException
                        ("접근 권한이 없습니다. userId = " + userIdFromHeader));

        return postsService.findAllByUserIdOpen(user.getUserId(), pageable);
    }

    @GetMapping("/gallery/{userId}")
    public List<GalleryResponse> getOtherGallery(@PathVariable Long userId, Pageable pageable) {
        return postsService.findAllByUserIdOpen(userId, pageable);
    }


}
