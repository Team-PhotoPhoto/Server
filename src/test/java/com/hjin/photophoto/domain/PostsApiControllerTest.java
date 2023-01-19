package com.hjin.photophoto.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.postsImg.PostsImg;
import com.hjin.photophoto.domain.postsImg.PostsImgRepository;
import com.hjin.photophoto.web.posts.dto.OpenUpdateRequest;
import com.hjin.photophoto.web.posts.dto.PostsSaveRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostsApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private PostsImgRepository postsImgRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeAll     //매번 테스트가 시작되기 전에 MockMvc 인스턴스를 생성
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach      //각 테스트 종료 후 실행, 리소스 제거 용
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
        postsImgRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Posts_등록하고_수정된다() throws Exception {

        //////////// 이미지 등록 ////////////
        String uploadImgUrl = "http://localhost:" + port + "/post/image";
        final String emptyBody = "{}";
        // when
        mvc.perform(post(uploadImgUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyBody))
                .andExpect(status().isOk());

        // then
        List<PostsImg> all = postsImgRepository.findAll();

        //////////// 코멘트 등록 ////////////
        Long postId = all.get(0).getPostId();
        String title = "title";
        String comments = "comments";
        String imageUrl = "imageUrl";
        String senderName = "senderName";
        Boolean readYn = false;
        Boolean openYn = false;
        Long receiverUserId = Long.valueOf(1);

        System.out.print("postId: ");
        System.out.println(postId);

        PostsSaveRequest requestDto = PostsSaveRequest.builder()
                .postId(postId)
                .title(title)
                .comments(comments)
                .imageUrl(imageUrl)
                .senderName(senderName)
                .receiverUserId(receiverUserId)
                .readYn(readYn)
                .openYn(openYn)
                .build();

        String savePostsUrl = "http://localhost:" + port + "/post";

        // when
        mvc.perform(post(savePostsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))    //본문 -> 문자열 JSOn
                .andExpect(status().isOk());

        // then
        List<Posts> all2 = postsRepository.findAll();
        assertThat(all2.get(0).getPostId()).isEqualTo(postId);
        assertThat(all2.get(0).getTitle()).isEqualTo(title);
        assertThat(all2.get(0).getComments()).isEqualTo(comments);
        assertThat(all2.get(0).getImageUrl()).isEqualTo(imageUrl);
        assertThat(all2.get(0).getSenderName()).isEqualTo(senderName);
        assertThat(all2.get(0).getReceiverUserId()).isEqualTo(receiverUserId);
        assertThat(all2.get(0).isReadYn()).isEqualTo(readYn);
        assertThat(all2.get(0).isOpenYn()).isEqualTo(openYn);

        //////////// 읽음 표시 ////////////
        String getPostsUrl = "http://localhost:" + port + "/post/" + postId;

        mvc.perform(get(getPostsUrl))
                .andExpect(status().isOk());

        // then
        List<Posts> allUpdatedPosts = postsRepository.findAll();
        System.out.println(allUpdatedPosts.get(0).isReadYn());
        assertThat(allUpdatedPosts.get(0).isReadYn()).isEqualTo(true);
        assertThat(allUpdatedPosts.get(0).isOpenYn()).isEqualTo(false);
    }
}
