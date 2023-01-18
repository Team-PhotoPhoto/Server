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

    @Column(length = 200, nullable = false, name = "image_url")
    private String imageUrl;

    @Column(length = 30, nullable = false, name = "sender_name")
    private String senderName;

    @Column(nullable = false, name = "receiver_user_id")
    private Long receiverUserId;

    @Column(nullable = false, name = "read")
    private boolean read;

    @Column(nullable = false, name = "open")
    private boolean open;

    @Builder        // 생성자
    public Posts(Long postId, String title, String comments, String imageUrl,
                 String senderName, Long receiverUserId,
                 boolean read, boolean open) {
        this.postId = postId;
        this.title = title;
        this.comments = comments;
        this.imageUrl = imageUrl;
        this.senderName = senderName;
        this.receiverUserId = receiverUserId;
        this.read = read;
        this.open = open;
    }

    public void updateOpen(boolean open) {
        this.open = open;
    }
    public void updateRead(boolean read) {
        this.read = read;
    }
}
