package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OpenUpdateRequest {

    private Long receiverUserId;

    @Builder
    public OpenUpdateRequest(Long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public Posts toEntity() {
        return Posts.builder()
                .receiverUserId(receiverUserId)
                .build();
    }

}
