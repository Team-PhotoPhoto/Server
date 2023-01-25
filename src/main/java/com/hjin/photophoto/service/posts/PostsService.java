package com.hjin.photophoto.service.posts;

import com.hjin.photophoto.domain.postsImg.PostsImg;
import com.hjin.photophoto.domain.postsImg.PostsImgRepository;
import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.subjects.SubjectsRepository;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.S3Exception;
//import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
//import software.amazon.awssdk.services.s3.presigner.S3Presigner;
//import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsImgRepository postsImgRepository;
    private final SubjectsRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubjectsResponse> findAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectsResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(PostsSaveRequest requestDto) {
        // PostImg 에서 삭제
        PostsImg postsImg = postsImgRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글의 이미지가 없습니다. postId = " + requestDto.getPostId()));

        postsImgRepository.delete(postsImg);

        return postsRepository.save(requestDto.toEntity())
                .getPostId();
    }

    @Transactional
    public Long uploadImage() {
        return postsImgRepository.save(PostsImg.builder().build())
                .getPostId();
    }

    @Transactional
    public Long updateOpen(Long postId) {
        // JPA: 트랜젝션 시작 후 변경 사항 모두 DB에 자동 저장(Dirty Checking)
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 포스트가 없습니다. postId = " + postId));

        // 데이터 값 변경
        posts.updateOpen(true);
        return postId;
    }

    @Transactional
    public void delete (Long postId, Long userIdFromHeader) throws AccessDeniedException {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다. postId = " + postId));

        if(!Objects.equals(posts.getReceiverUserId(), userIdFromHeader)) {
            throw new AccessDeniedException("해당 게시글을 수정할 수 있는 유저가 아닙니다. postId = " + userIdFromHeader);
        } else {
            postsRepository.delete(posts);
        }
    }

    @Transactional
    public Long updateRead(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 포스트가 없습니다. postId = " + postId));

        posts.updateRead(true);
        return postId;


    }

    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public PostsResponse findByPostId(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다. postId=" + postId));

        return new PostsResponse(entity);

    }

    // userId로 찾기, inbox
    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public List<InboxResponse> findAllByUserId(Long receiverUserId, Pageable pageable) {
        userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + receiverUserId));

        return postsRepository.findPostsByReceiverUserIdOrderByCreatedDateDesc(receiverUserId, pageable)
                .stream()
                .map(InboxResponse::new)
                .collect(Collectors.toList());
    }

    // userId로 찾기, gallery(isOpened == true)
    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public List<GalleryResponse> findAllByUserIdOpen(Long receiverUserId, Pageable pageable) {
        userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + receiverUserId));

        return postsRepository
                .findPostsByReceiverUserIdAndOpenYnOrderByCreatedDateDesc(receiverUserId, true, pageable)
                .stream()
                .map(GalleryResponse::new)
                .collect(Collectors.toList());
    }

}


//트랜잭션: 커넥션을 연결해주는 의미(어플리케이션 단으로)
// 트랜잭션이 없다면 조회를 하고 만약 두 세 개라면 조회할 때마다 커넥션을 연결해야 함
//트랜잭션이 있으면 하나의 연결을 가지고 여러번 연결을 할 수 있음
//어노테이션 선언: 그 커넥션을 같이 이용하겠다
//readOnly: Db는 하나인데 트래픽 부하 등 디비 장애가 올 수도 있어서 분산시키려, read replica


//IllegalArgumentException: 예외에 변수를 넣을 수 있음 -> 포스트가 없을 떄 어떻게 처리하는지 등등
//1 나만의 커스텀 exception 을 만들어도 좋음 -> 통계 내기 좋음, 분석 (추천)
//2 하나의 예외인데 타입 파라미터(enum) 추가