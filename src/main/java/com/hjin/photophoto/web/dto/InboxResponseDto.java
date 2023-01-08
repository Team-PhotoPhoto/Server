package com.hjin.photophoto.web.dto;

import com.hjin.photophoto.domain.posts.Posts;

import java.time.LocalDateTime;

public class InboxResponseDto {

    private Long postId;
    private String title;
    private String senderName;
    private int isRead;
    private LocalDateTime createdDate;

    public InboxResponseDto(Posts entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.senderName = entity.getSenderName();
        this.isRead = entity.getIsRead();
        this.createdDate = entity.getCreatedDate();
    }
}
