package com.hjin.photophoto.service.auth;

import com.hjin.photophoto.domain.auth.AuthRepository;
import com.hjin.photophoto.web.auth.dto.SignUpRequest;
import com.hjin.photophoto.web.posts.dto.PostsSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthRepository authRepository;

    @Transactional
    public Long save(SignUpRequest requestDto) {
        // 비밀번호 암호화
        String rawPassword = requestDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode((rawPassword));
        requestDto.setPassword(encPassword);
        return authRepository.save(requestDto.toEntity())
                .getAuthId();
    }
}
