package com.hjin.photophoto.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트 제목";
        String comments = "테스트 본문";
        String imageUrl = "테스트 이미지";
        String senderName = "테스트 보내는 사람 닉네임";
        String senderUserId = "테스트 보내는 사람 아이디";
        String receiverUserId = "테스트 받는 사람 아이디";
        Boolean isRead = false;
        Boolean isOpen = false;

        //insert, update
        postsRepository.save(Posts.builder()
                .title(title)
                .comments(comments)
                .imageUrl(imageUrl)
                .senderName(senderName)
                .senderUserId(senderUserId)
                .receiverUserId(receiverUserId)
                .isRead(isRead)
                .isOpen(isOpen)
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getComments()).isEqualTo(comments);


    }

//    @Test
//    public void BaseTimeEntity_등록() {
//        //given
//        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
//        postsRepository.save(Posts.builder()
//                .title("title")
//                .content("content")
//                .author("author")
//                .build());
//
//        //when
//        List<Posts> postsList = postsRepository.findAll();
//
//        //then
//        Posts posts = postsList.get(0);
//
//        System.out.println(">>>>>>  createDate = " + posts.getCreatedDate() + ", modifiedDate = " + posts.getModifiedDate());
//
//        assertThat(posts.getCreatedDate()).isAfter(now);
//        assertThat(posts.getModifiedDate()).isAfter(now);
//    }
}
