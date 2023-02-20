package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.dto.OAuthAttributes;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.refreshToken.RefreshTokenRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.domain.view.ViewRepository;
import com.hjin.photophoto.service.view.ViewService;
import lombok.RequiredArgsConstructor;
import org.hibernate.loader.collection.OneToManyJoinWalker;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final ViewService viewService;
    private final ViewRepository viewRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //어떤 소셜 로그인인지(구글, 네이버, ..)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth2 로그인 진행 시 키가 되는 필드값, 구글은 기본코드 'sub'
        String userNameAttributeName =
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        // 유저 정보
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // 기본 이미지 url 설정
        user.updateImage(user.getUserId());

//        System.out.println("attributes.getAttributes(): " + attributes.getAttributes());


//
//        // userId 추가
////        Map<String, Object> customAttribute = attributes.getAttributes();
////        customAttribute.put("userId", user.getUserId());
//        Map<String, Object> customAttribute = new LinkedHashMap<>();
//        customAttribute.put("email", user.getEmailAuth());
//        customAttribute.put("userId", user.getUserId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmailAuth(attributes.getEmailAuth())
                .orElse(attributes.toEntity());

        // userEntity 저장
        viewRepository.findByUserId(user.getUserId())
                        .orElse(viewService.saveView(user.getUserId()));
        return userRepository.save(user);
    }



}

