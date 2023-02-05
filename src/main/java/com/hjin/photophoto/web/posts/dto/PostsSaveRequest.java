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
    private final String thumbnailUrl;
    private final String originUrl;
    private final String senderName;
    private final Long receiverUserId;
    private final boolean readYn;
    private final boolean openYn;


    @Builder
    public PostsSaveRequest(Long postId, String title, String comments,
                            String senderName, Long receiverUserId, Boolean readYn, Boolean openYn) {
        this.postId = postId;
        this.title = title;
        this.comments = comments;
        this.thumbnailUrl = "https://photophoto-posts-img.s3.ap-northeast-2.amazonaws.com/thumbnail/" + postId + ".png";
        this.originUrl = "https://photophoto-posts-img.s3.ap-northeast-2.amazonaws.com/origin/" + postId + ".png";
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
                .thumbnailUrl(thumbnailUrl)
                .originUrl(originUrl)
                .senderName(senderName)
                .receiverUserId(receiverUserId)
                .readYn(readYn)
                .openYn(openYn)
                .build();

    }
}
