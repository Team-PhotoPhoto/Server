package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import java.time.LocalDateTime;

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
