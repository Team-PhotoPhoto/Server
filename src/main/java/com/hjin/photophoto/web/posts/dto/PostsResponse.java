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
    private String thumbnailUrl;
    private String originUrl;
    private String senderName;
    private Long receiverUserId;
    private LocalDateTime createdDate;
    private boolean open;

    public PostsResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.comments = entity.getComments();
        this.thumbnailUrl = entity.getThumbnailUrl();
        this.originUrl = entity.getOriginUrl();
        this.senderName = entity.getSenderName();
        this.receiverUserId = entity.getReceiverUserId();
        this.createdDate = entity.getCreatedDate();
        this.open = entity.isOpenYn();
    }
}
