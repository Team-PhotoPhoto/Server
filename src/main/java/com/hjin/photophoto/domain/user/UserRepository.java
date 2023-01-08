package com.hjin.photophoto.domain.user;

import com.hjin.photophoto.domain.posts.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Posts, Long> {
}
