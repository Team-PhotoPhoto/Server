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
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //PK 생성 규칙, auto increment
    private Long postId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 300, nullable = false)
    private String comments;

    @Column(length = 200, nullable = false)
    private String imageUrl;

    @Column(length = 30, nullable = false)
    private String senderName;

    @Column(nullable = false)
    private Long receiverUserId;

    @Column(nullable = false)
    private boolean read;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
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
