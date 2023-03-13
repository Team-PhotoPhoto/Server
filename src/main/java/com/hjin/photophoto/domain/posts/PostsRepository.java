package com.hjin.photophoto.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

//    Optional<Posts> findByPostId(Long postId);
    Page<Posts> findPostsByReceiverUserIdAndOpenYnOrderByCreatedDateDesc(Long receiverUserId, boolean openYn, Pageable pageable);

    //읽지 않은 포스트 확인
    Optional<Long> countByReceiverUserIdAndReadYn(Long receiverUserId, boolean readYn);

    Long deletePostsByReceiverUserId(Long receiverUserId);
}


