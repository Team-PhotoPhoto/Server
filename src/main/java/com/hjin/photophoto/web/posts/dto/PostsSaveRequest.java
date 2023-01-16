package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequest {

    private Long postId;

    private String title;
    private String comments;
    private String imageUrl;
    private String senderName;
    private Long receiverUserId;
    private boolean read;
    private boolean open;

    @Builder
    public PostsSaveRequest(Long postId, String title, String comments, String imageUrl,
                            String senderName, Long receiverUserId) {
        this.postId = postId;
        this.title = title;
        this.comments = comments;
        this.imageUrl = imageUrl;
        this.senderName = senderName;
        this.receiverUserId = receiverUserId;
        this.read = false;
        this.open = false;
    }

    public Posts toEntity() {
        return Posts.builder()
                .postId(postId)
                .title(title)
                .comments(comments)
                .imageUrl(imageUrl)
                .senderName(senderName)
                .receiverUserId(receiverUserId)
                .read(read)
                .open(open)
                .build();

    }
}
