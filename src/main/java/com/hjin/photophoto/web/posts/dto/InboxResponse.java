package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;

import java.time.LocalDateTime;

public class InboxResponse {

    private Long postId;
    private String title;
    private String senderName;
    private boolean read;
    private LocalDateTime createdDate;

    public InboxResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.senderName = entity.getSenderName();
        this.read = entity.isRead();
        this.createdDate = entity.getCreatedDate();
    }
}
