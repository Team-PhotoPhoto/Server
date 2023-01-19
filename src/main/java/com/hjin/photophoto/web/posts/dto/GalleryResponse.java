package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter     //setter 생성x
public class GalleryResponse {

    private Long postId;
    private String imageUrl;
    private LocalDateTime createdDate;

    public GalleryResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.imageUrl = entity.getImageUrl();
        this.createdDate = entity.getCreatedDate();
    }
}
