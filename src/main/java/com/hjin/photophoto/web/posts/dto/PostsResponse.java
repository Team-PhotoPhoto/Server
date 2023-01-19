package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter     //setter 생성x

public class PostsResponse {

    private Long postId;
    private String title;
    private String comments;
    private String imageUrl;
    private String senderName;
    private Long receiverUserId;
    private LocalDateTime createdDate;

    public PostsResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.comments = entity.getComments();
        this.imageUrl = entity.getImageUrl();
        this.senderName = entity.getSenderName();
        this.receiverUserId = entity.getReceiverUserId();
        this.createdDate = entity.getCreatedDate();
    }
}
