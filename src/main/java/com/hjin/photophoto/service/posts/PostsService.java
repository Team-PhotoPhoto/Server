package com.hjin.photophoto.service.posts;

import com.amazonaws.HttpMethod;
import com.hjin.photophoto.domain.postsImg.PostsImg;
import com.hjin.photophoto.domain.postsImg.PostsImgRepository;
import com.hjin.photophoto.domain.posts.Posts;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.subjects.SubjectsRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.service.ImageService;
import com.hjin.photophoto.web.posts.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsImgRepository postsImgRepository;
    private final SubjectsRepository subjectRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final JavaMailSender javaMailSender;

    @Transactional
    public String getPostUploadUrl(Long postId, String type) {
        return imageService.getSingedUrl(2, type, postId, HttpMethod.PUT);
    }

    @Transactional(readOnly = true)
    public List<SubjectsResponse> findAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectsResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long save(PostsSaveRequest requestDto) {
        // PostImg 에서 삭제
        PostsImg postsImg = postsImgRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_IMAGE_POST, requestDto.getPostId()));

        postsImgRepository.delete(postsImg);

        return postsRepository.save(requestDto.toEntity())
                .getPostId();
    }

    @Transactional
    public Long uploadImage() {
        return postsImgRepository.save(PostsImg.builder().build())
                .getPostId();
    }

    @Transactional
    public Long updateOpen(Long postId, Long userIdFromHeader) {
        // JPA: 트랜젝션 시작 후 변경 사항 모두 DB에 자동 저장(Dirty Checking)
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_POST, postId));

        if (!Objects.equals(posts.getReceiverUserId(), userIdFromHeader)) {
            throw new MyException(MyExceptionType.NO_PERMISSION, userIdFromHeader);
        } else {
            posts.updateOpen(true);
            return postId;
        }
    }

    @Transactional
    public void delete (Long postId, Long userIdFromHeader) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_POST, postId));

        if(!Objects.equals(posts.getReceiverUserId(), userIdFromHeader)) {
            throw new MyException(MyExceptionType.NO_PERMISSION, userIdFromHeader);
        } else {
            postsRepository.delete(posts);
        }
    }

    @Transactional
    public void updateRead(Long postId) {
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_POST, postId));

        posts.updateRead(true);
    }

    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public PostsResponse findByPostId(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_POST, postId));

        return new PostsResponse(entity);

    }

    // userId로 찾기, inbox
    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public List<InboxResponse> findAllByUserId(Long receiverUserId, Pageable pageable) {
        userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, receiverUserId));

        return postsRepository.findPostsByReceiverUserIdOrderByCreatedDateDesc(receiverUserId, pageable)
                .stream()
                .map(InboxResponse::new)
                .collect(Collectors.toList());
    }

    // userId로 찾기, gallery(isOpened == true)
    @Transactional(readOnly = true)     // 조회 기능만 남아 속도 향상
    public List<GalleryResponse> findAllByUserIdOpen(Long receiverUserId, Pageable pageable) {
        userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, receiverUserId));

        return postsRepository
                .findPostsByReceiverUserIdAndOpenYnOrderByCreatedDateDesc(receiverUserId, true, pageable)
                .stream()
                .map(GalleryResponse::new)
                .collect(Collectors.toList());
    }

    public void sendMail(PostsSaveRequest requestDto) {

        User user = userRepository.findByUserId(requestDto.getReceiverUserId())
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, requestDto.getReceiverUserId()));

        // 수신 여부 확인
        if (user.isNoti()) {
            MimeMessage message = javaMailSender.createMimeMessage();

            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                // 1. 메일 수신자, 발신자 설정
                messageHelper.setTo(user.getEmailNoti());
                messageHelper.setFrom("photophoto.official@gmail.com", "포토포토");

                // 2. 메일 제목 설정
                messageHelper.setSubject("[포토포토] 갤러리 수신함에 방명록이 도착했어!");

                // 3. 메일 내용 설정
                // HTML 적용됨
                String content = setMailBody(user.getUserId(), user.getNickname(), requestDto.getTitle());
//                System.out.println(content);
                messageHelper.setText(content, true);

                // 4. 메일 전송
                javaMailSender.send(message);

            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        }



    }

    private String setMailBody (Long userId, String nickname, String title) {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "        <title>HTML Email Template</title>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"bodyTable\">\n" +
                "            <tr>\n" +
                "                <td align=\"center\">\n" +
                "                    <!-- 600px - 800px CONTENTS CONTAINER TABLE -->\n" +
                "                    <center style=\"width:100%\">\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"align-self: center; padding-top: 40px; padding-bottom: 40px; background-color: #ffffff; text-align: center\">\n" +
                "                            <tr>\n" +
                "                                <td>\n" +
                "                                    <img alt=\"\" id=\"thumb\" width=\"214px\" height=\"214px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/mailbox.png\" style=\"align-self: center;\">\n" +
                "                                    <p id=\"sender\" style=\"font-size: 20px; font-weight: 500; text-align: center; color: #989898; padding-top: 50px; padding-bottom: 16px\">"+nickname+"</p>\n" +
                "                                    <span id=\"title\" style=\"font-size: 24px; font-weight: 700; text-align: center; color: #484848;\">"+title+"</span><br>\n" +
                "                                    <p id=\"ask_title\" style=\"font-size: 16px; font-weight: 500; text-align: center; color: #535353; padding-top: 50px;\">지금 바로 확인해 볼래?</p>\n" +
                "                                    <a href=\"http://www.photophoto.me/gallery/"+userId+"\">\n" +
                "                                        <img alt=\"\" id=\"button\" width=\"286px\" height=\"52px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/btn.png\n\" style=\"align-self: center; padding-top: 8px;\">\n" +
                "                                    </a>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </center>\n" +
                "\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>\n" +
                "                    <!-- 600px - 800px CONTENTS CONTAINER TABLE -->\n" +
                "                    <center width=\"100%\">\n" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "                            <tr>\n" +
                "                                <td style=\"padding-top: 10px; padding-bottom: 10px; background-color: #657588; text-align: center;\">\n" +
                "                                    <img alt=\"\" id=\"logo\" width=\"63px\" height=\"63px\" src=\"https://photophoto-img.s3.ap-northeast-2.amazonaws.com/logo.png\" style=\"align-self: center;\">\n" +
                "                                    <p id=\"footer_content\" style=\"font-size: 14px; font-weight: 500; text-align: center; color: #ffffff; margin-top: 10px; margin-bottom: 40px;\">즐거운 추억을 친구들과 함께 공유하자!</p>\n" +
                "                                    <span id=\"footer_copyright\" style=\"font-size: 12px; font-weight: 700; text-align: center; color: #ffffff;\">© 2023 PhotoPhoto All rights reserved.</span><br>\n" +
                "                                    <span id=\"discript_block\" style=\"font-size: 9px; font-weight: 400; text-align: center; color: #ffffff;\">*메일 수신 거부는 <span style=\"text-decoration: underline;\">프로필 설정</span>에서 할 수 있어요</span>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </table>\n" +
                "                    </center>\n" +
                "                </td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </body>\n" +
                "</html>";
    }


}


//트랜잭션: 커넥션을 연결해주는 의미(어플리케이션 단으로)
// 트랜잭션이 없다면 조회를 하고 만약 두 세 개라면 조회할 때마다 커넥션을 연결해야 함
//트랜잭션이 있으면 하나의 연결을 가지고 여러번 연결을 할 수 있음
//어노테이션 선언: 그 커넥션을 같이 이용하겠다
//readOnly: Db는 하나인데 트래픽 부하 등 디비 장애가 올 수도 있어서 분산시키려, read replica


//IllegalArgumentException: 예외에 변수를 넣을 수 있음 -> 포스트가 없을 떄 어떻게 처리하는지 등등
//1 나만의 커스텀 exception 을 만들어도 좋음 -> 통계 내기 좋음, 분석 (추천)
//2 하나의 예외인데 타입 파라미터(enum) 추가