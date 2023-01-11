package com.hjin.photophoto.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findAuthByEmail_auth(String email_auth);

    static final String UPDATE_AUTH_LAST_LOGIN =
            "UPDATE Auth SET lastLoginDate = :lastLoginDate " +
                    "WHERE email_noti = :email_noti";
}
