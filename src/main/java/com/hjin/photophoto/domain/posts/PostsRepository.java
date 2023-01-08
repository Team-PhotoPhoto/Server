package com.hjin.photophoto.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("""
            SELECT p FROM Posts p WHERE p.receiverUserId = :userId ORDER BY p.createdDate DESC
            """
            )
    List<Posts> findAllByUserIdDesc(@Param("userId") String receiverUserId);

    List<Posts> findPostsByReceiverUserIdOrderByCreatedDateDesc(String receiverUserId);
}


