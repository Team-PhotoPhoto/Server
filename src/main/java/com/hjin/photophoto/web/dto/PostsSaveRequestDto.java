package com.hjin.photophoto.web.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String comments;
    private String imageUrl;
    private String senderName;
    private String receiverUserId;
    private int isRead;
    private int isOpened;

    @Builder
    public PostsSaveRequestDto(String title, String comments, String imageUrl,
                               String senderName, String receiverUserId) {
        this.title = title;
        this.comments = comments;
        this.imageUrl = imageUrl;
        this.senderName = senderName;
        this.receiverUserId = receiverUserId;
        this.isRead = 0;
        this.isOpened = 0;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .comments(comments)
                .imageUrl(imageUrl)
                .senderName(senderName)
                .receiverUserId(receiverUserId)
                .isRead(isRead)
                .isOpened(isOpened)
                .build();

    }
}
