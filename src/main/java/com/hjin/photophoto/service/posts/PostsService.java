package com.hjin.photophoto.service.posts;

import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity())
                .getPostId();
    }

    @Transactional
    public Long update(Long postId, PostsUpdateRequestDto requestDto) {
        // JPA: 트랜젝션 시작 후 변경 사항 모두 DB에 자동 저장(Dirty Checking)
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다. postId = " + postId));

        // 데이터 값 변경
        posts.update(requestDto.getIsOpened());

        return postId;
    }

    public PostsResponseDto findByPostId (Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다. postId = " + postId));

        return new PostsResponseDto(entity);
    }

    // userId로 찾기, inbox
    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public List<InboxResponseDto> findAllByUserId(String receiverUserId) {
        return postsRepository.findAllByUserIdDesc(receiverUserId).stream()
                .map(InboxResponseDto::new)
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