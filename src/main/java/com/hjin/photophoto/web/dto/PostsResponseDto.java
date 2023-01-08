package com.hjin.photophoto.web.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Builder;

public class PostsResponseDto {


    private String title;
    private String comments;
    private String imageUrl;
    private String senderName;
    private String receiverUserId;

    public PostsResponseDto(Posts entity) {
        this.title = entity.getTitle();
        this.comments = entity.getComments();
        this.imageUrl = entity.getImageUrl();
        this.senderName = entity.getSenderName();
        this.receiverUserId = entity.getReceiverUserId();
    }
}
