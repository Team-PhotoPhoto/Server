package com.hjin.photophoto.domain.user;

import com.hjin.photophoto.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAuth(String emailAuth);
    Optional<User> findByUserId(Long userId);
}
