package com.hjin.photophoto.service.user;

import com.amazonaws.HttpMethod;
import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.domain.view.View;
import com.hjin.photophoto.domain.view.ViewRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.service.ImageService;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final ImageService imageService;
    private final ViewRepository viewRepository;
    private final PostsRepository postsRepository;


    @Transactional
    public String getUserUploadUrl(Long userId) {
        return imageService.getSingedUrl(10, "user", userId, HttpMethod.PUT);
    }


//    @Transactional
//    public Long save(UserSaveRequest requestDto) {
//        return userRepository.save(requestDto.toEntity())
//                .getUserId();
//    }

    @Transactional
    public Long update (Long userId, UserUpdateRequest requestDto) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, userId));

        user.update(requestDto.getNickname(),
                requestDto.getFrameType(), requestDto.getWallType(),requestDto.getEmailNoti(), requestDto.isNoti());

        return userId;
    }

    @Transactional(readOnly = true)
    public UserResponse findByUserId (Long userId) {
        User entity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, userId));

        View view = viewRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_VIEW, userId));

        return new UserResponse(entity, view.getCount());
    }

    @Transactional
    public void delete (Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException(MyExceptionType.NOT_EXIST_USER, userId));

        // 해당 유저가 받은 Post 삭제
        postsRepository.deletePostsByReceiverUserId(userId);

        userRepository.delete(user);
    }

}
