package com.hjin.photophoto.config.auth.dto;
import com.hjin.photophoto.domain.user.Role;
import com.hjin.photophoto.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String emailAuth;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String emailAuth) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.emailAuth = emailAuth;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
//        System.out.println("attributes");
//        System.out.println(attributes);

//        attributes.put("provider", registrationId);
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        if("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
//        System.out.println(userNameAttributeName);
        return OAuthAttributes.builder()
                .emailAuth((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver (String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .emailAuth((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao (String userNameAttributeName, Map<String, Object> attributes) {

        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        System.out.println(response.get("email"));
        return OAuthAttributes.builder()
                .emailAuth((String) response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {    // 처음 가입할 때 User entity 생성, JOIN 으로
        return User.builder()
                .emailAuth(emailAuth)
                .role(Role.JOIN)
                .build();
    }
}

