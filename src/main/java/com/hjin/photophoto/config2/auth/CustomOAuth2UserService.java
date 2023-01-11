package com.hjin.photophoto.config2.auth;

import com.hjin.photophoto.config2.auth.dto.OAuthAttributes;
import com.hjin.photophoto.config2.auth.dto.SessionUser;
import com.hjin.photophoto.domain.auth.Auth;
import com.hjin.photophoto.domain.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AuthRepository authRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //어떤 소셜 로그인인지(구글, 네이버, ..)
        String userNameAttributeName = userRequest  //카카오 네이버 OAuth2 로그인 pk, 구글은 기본코드 'sub'
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Auth auth = authRepository.findAuthByEmail_auth(attributes.getEmail_auth())
                .orElse(attributes.toEntity());

        httpSession.setAttribute("user", new SessionUser(auth)); //SessionUser: 세션에 사용자 정보 저장하기 위한 dto

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(auth.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

}

