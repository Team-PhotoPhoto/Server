package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter     //setter 생성x
public class GalleryResponse {

    private Long postId;
    private String thumbnailUrl;
    private String originUrl;
    private LocalDateTime createdDate;

    public GalleryResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.originUrl = entity.getOriginUrl();
        this.createdDate = entity.getCreatedDate();
    }
}
