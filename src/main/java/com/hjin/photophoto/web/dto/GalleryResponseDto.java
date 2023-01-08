package com.hjin.photophoto.web.dto;

import com.hjin.photophoto.domain.posts.Posts;
import java.time.LocalDateTime;

public class GalleryResponseDto {


    private Long postId;
    private String imageUrl;
    private LocalDateTime createdDate;

    public GalleryResponseDto(Posts entity) {
        this.postId = entity.getPostId();
        this.imageUrl = entity.getImageUrl();
        this.createdDate = entity.getCreatedDate();
    }
}
