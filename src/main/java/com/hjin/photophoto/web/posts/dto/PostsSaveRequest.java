package com.hjin.photophoto.web.posts.dto;

import com.hjin.photophoto.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostsSaveRequest {

    private final Long postId;
    private final String title;
    private final String comments;
    private final String imageUrl;
    private final String senderName;
    private final Long receiverUserId;
    private final boolean readYn;
    private final boolean openYn;


    @Builder
    public PostsSaveRequest(Long postId, String title, String comments, String imageUrl,
                            String senderName, Long receiverUserId, Boolean readYn, Boolean openYn) {
        this.postId = postId;
        this.title = title;
        this.comments = comments;
        this.imageUrl = imageUrl;
        this.senderName = senderName;
        this.receiverUserId = receiverUserId;
        this.readYn = readYn;
        this.openYn = openYn;
    }

    public Posts toEntity() {
        return Posts.builder()
                .postId(postId)
                .title(title)
                .comments(comments)
                .imageUrl(imageUrl)
                .senderName(senderName)
                .receiverUserId(receiverUserId)
                .readYn(readYn)
                .openYn(openYn)
                .build();

    }
}
