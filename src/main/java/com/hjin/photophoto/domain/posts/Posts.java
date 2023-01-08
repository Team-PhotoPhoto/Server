package com.hjin.photophoto.domain.posts;


import com.hjin.photophoto.domain.BaseTimeEntity;
import com.hjin.photophoto.domain.user.User;
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

    @ManyToOne
    @JoinColumn(name = "receiverUserId")
    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isRead;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isOpened;

    @Builder        // 생성자
    public Posts(String title, String comments, String imageUrl,
                 String senderName, User user,
                 boolean isRead, boolean isOpened) {
        this.title = title;
        this.comments = comments;
        this.imageUrl = imageUrl;
        this.senderName = senderName;
        this.user = user;
        this.isRead = isRead;
        this.isOpened = isOpened;
    }

    public void update(boolean isOpened) {
        this.isOpened = isOpened;
    }
}
