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
        // PostImg ?????? ??????
        PostsImg postsImg = postsImgRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_IMAGE_POST, requestDto.getPostId()));

        postsImgRepository.delete(postsImg);

        return postsRepository.save(requestDto.toEntity())
                .getPostId();
    }

    @Transactional
    public Long saveMe(PostsSaveMeRequest requestDto) {
        // PostImg ?????? ??????
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
        // JPA: ???????????? ?????? ??? ?????? ?????? ?????? DB??? ?????? ??????(Dirty Checking)
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

    @Transactional(readOnly = true)     // ?????? ????????? ?????? ?????? ??????
    public PostsResponse findByPostId(Long postId) {
        Posts entity = postsRepository.findById(postId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_POST, postId));

        return new PostsResponse(entity);

    }

    // userId??? ??????, inbox
    @Transactional(readOnly = true)     // ?????? ????????? ?????? ?????? ??????
    public List<InboxResponse> findAllByUserIdClosed(Long receiverUserId, Pageable pageable) {
        userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, receiverUserId));

        return postsRepository.findPostsByReceiverUserIdAndOpenYnOrderByCreatedDateDesc(receiverUserId, false, pageable)
                .stream()
                .map(InboxResponse::new)
                .collect(Collectors.toList());
    }

    // userId??? ??????, gallery(isOpened == true)
    @Transactional(readOnly = true)     // ?????? ????????? ?????? ?????? ??????
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

        // ?????? ?????? ??????
        if (user.isNoti()) {
            MimeMessage message = javaMailSender.createMimeMessage();

            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

                // 1. ?????? ?????????, ????????? ??????
                messageHelper.setTo(user.getEmailNoti());
                messageHelper.setFrom("photophoto.official@gmail.com", "????????????");

                // 2. ?????? ?????? ??????
                messageHelper.setSubject("[????????????] ????????? ???????????? ???????????? ????????????!");

                // 3. ?????? ?????? ??????
                // HTML ?????????
                String content = setMailBody(user.getUserId(), user.getNickname(), requestDto.getTitle());
//                System.out.println(content);
                messageHelper.setText(content, true);

                // 4. ?????? ??????
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
                "                                    <p id=\"ask_title\" style=\"font-size: 16px; font-weight: 500; text-align: center; color: #535353; padding-top: 50px;\">?????? ?????? ????????? ???????</p>\n" +
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
                "                                    <p id=\"footer_content\" style=\"font-size: 14px; font-weight: 500; text-align: center; color: #ffffff; margin-top: 10px; margin-bottom: 40px;\">????????? ????????? ???????????? ?????? ????????????!</p>\n" +
                "                                    <span id=\"footer_copyright\" style=\"font-size: 12px; font-weight: 700; text-align: center; color: #ffffff;\">?? 2023 PhotoPhoto All rights reserved.</span><br>\n" +
                "                                    <span id=\"discript_block\" style=\"font-size: 9px; font-weight: 400; text-align: center; color: #ffffff;\">*?????? ?????? ????????? <span style=\"text-decoration: underline;\">????????? ??????</span>?????? ??? ??? ?????????</span>\n" +
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


//????????????: ???????????? ??????????????? ??????(?????????????????? ?????????)
// ??????????????? ????????? ????????? ?????? ?????? ??? ??? ????????? ????????? ????????? ???????????? ???????????? ???
//??????????????? ????????? ????????? ????????? ????????? ????????? ????????? ??? ??? ??????
//??????????????? ??????: ??? ???????????? ?????? ???????????????
//readOnly: Db??? ???????????? ????????? ?????? ??? ?????? ????????? ??? ?????? ????????? ???????????????, read replica


//IllegalArgumentException: ????????? ????????? ?????? ??? ?????? -> ???????????? ?????? ??? ????????? ??????????????? ??????
//1 ????????? ????????? exception ??? ???????????? ?????? -> ?????? ?????? ??????, ?????? (??????)
//2 ????????? ???????????? ?????? ????????????(enum) ??????