package com.hjin.photophoto.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

//    Optional<Posts> findByPostId(Long postId);
    Page<Posts> findPostsByReceiverUserIdOrderByCreatedDateDesc(Long receiverUserId, Pageable pageable);
    Page<Posts> findPostsByReceiverUserIdAndOpenYnOrderByCreatedDateDesc(Long receiverUserId, boolean openYn, Pageable pageable);

}


