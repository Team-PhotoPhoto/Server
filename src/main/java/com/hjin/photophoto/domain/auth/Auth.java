package com.hjin.photophoto.domain.auth;

import com.hjin.photophoto.domain.BaseTimeEntity;
import com.hjin.photophoto.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Auth extends BaseTimeEntity {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

    @Column(length = 400, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String socialUserId;

    @Column(length = 100, nullable = false)
    private String email_auth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    @Builder        // 생성자
    public Auth(Long authId, String password, String socialUserId,
                String email_auth, Role role, LocalDateTime lastLoginDate) {
        this.authId = authId;
        this.password = password;
        this.socialUserId = socialUserId;
        this.email_auth = email_auth;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }


}
