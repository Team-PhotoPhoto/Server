package com.hjin.photophoto.domain.user;

import com.hjin.photophoto.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
