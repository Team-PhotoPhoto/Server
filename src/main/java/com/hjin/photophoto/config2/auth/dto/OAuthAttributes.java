package com.hjin.photophoto.config2.auth.dto;
import com.hjin.photophoto.domain.auth.Auth;
import com.hjin.photophoto.domain.auth.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email_auth;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email_auth) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email_auth = email_auth;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver (String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Auth toEntity() {    // 처음 가입할 때 User entity 생성, GUEST 로
        return Auth.builder()
                .role(Role.GUEST)
                .build();
    }
}

