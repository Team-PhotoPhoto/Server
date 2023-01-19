package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter     //setter 생성x

public class InboxResponse {

    private Long postId;
    private String title;
    private String senderName;
    private boolean readYn;
    private LocalDateTime createdDate;

    public InboxResponse(Posts entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.senderName = entity.getSenderName();
        this.readYn = entity.isReadYn();
        this.createdDate = entity.getCreatedDate();
    }
}
