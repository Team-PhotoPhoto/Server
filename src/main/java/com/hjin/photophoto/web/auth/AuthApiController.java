package com.hjin.photophoto.web.auth;

import com.hjin.photophoto.domain.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthApiController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signUpProc")
    public String signUpProc(Auth auth) {
        String rawPassword = auth.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);


    }
}
