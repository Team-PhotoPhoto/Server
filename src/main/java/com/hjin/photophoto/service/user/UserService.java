package com.hjin.photophoto.service.user;

import com.hjin.photophoto.domain.user.User;
import com.hjin.photophoto.domain.user.UserRepository;
import com.hjin.photophoto.web.user.dto.UserResponse;
import com.hjin.photophoto.web.user.dto.UserSaveRequest;
import com.hjin.photophoto.web.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

//    @Transactional
//    public Long save(UserSaveRequest requestDto) {
//        return userRepository.save(requestDto.toEntity())
//                .getUserId();
//    }

    @Transactional
    public Long update (Long userId, UserUpdateRequest requestDto) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + userId));


        user.update(requestDto.getImageUrl(),requestDto.getNickname(),
                requestDto.getFrameType(), requestDto.getWallType(),requestDto.getEmailNoti(), requestDto.isNoti());

        return userId;
    }

    @Transactional(readOnly = true)
    public UserResponse findByUserId (Long userId) {
        User entity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));

        return new UserResponse(entity);
    }

    @Transactional
    public void updateDeleteByUserId (Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException
                        ("해당 유저가 없습니다. userId = " + userId));

        user.updateDelete();
        return;
    }
}
