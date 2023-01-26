package com.hjin.photophoto.domain.posts;


import com.hjin.photophoto.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id     //PK
    @Column(name = "post_id")
    private Long postId;

    @Column(length = 50, nullable = false, name = "title")
    private String title;

    @Column(length = 300, nullable = false, name = "comments")
    private String comments;

    @Column(length = 300, nullable = false, name = "thumbnail_url")
    private String thumbnailUrl;
    @Column(length = 300, nullable = false, name = "origin_url")
    private String originUrl;

    @Column(length = 30, nullable = false, name = "sender_name")
    private String senderName;

    @Column(nullable = false, name = "receiver_user_id")
    private Long receiverUserId;

    @Column(nullable = false, name = "read_yn")
    private boolean readYn;

    @Column(nullable = false, name = "open_yn")
    private boolean openYn;

    @Builder        // 생성자
    public Posts(Long postId, String title, String comments, String thumbnailUrl, String originUrl,
                 String senderName, Long receiverUserId,
                 boolean readYn, boolean openYn) {
        this.postId = postId;
        this.title = title;
        this.comments = comments;
        this.thumbnailUrl = thumbnailUrl;
        this.originUrl = originUrl;
        this.senderName = senderName;
        this.receiverUserId = receiverUserId;
        this.readYn = readYn;
        this.openYn = openYn;
    }

    public void updateOpen(boolean openYn) {
        this.openYn = openYn;
    }
    public void updateRead(boolean readYn) {
        this.readYn = readYn;
    }
}
