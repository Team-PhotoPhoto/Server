package com.hjin.photophoto.domain.refreshToken;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "token_key", nullable = false)
    private Long key;

    @Column(name = "token_value", nullable = false)
    private String token;

    @Builder
    public RefreshToken(Long key, String token) {
        this.key = key;
        this.token = token;
    }

    public RefreshToken updateToken (String token) {
        this.token = token;
        return this;
    }
}
