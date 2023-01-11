package com.hjin.photophoto.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Optional<Posts> findPostsByPostId(Long postId);
    List<Posts> findPostsByReceiverUserOrderByCreatedDateDesc(Long receiverUserId);
    List<Posts> findPostsByReceiverUserIdAndOpenOrderByCreatedDateDesc(Long receiverUserId, boolean open);
}


