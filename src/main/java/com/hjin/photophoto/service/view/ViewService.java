package com.hjin.photophoto.service.view;


import com.hjin.photophoto.domain.posts.PostsRepository;
import com.hjin.photophoto.domain.view.View;
import com.hjin.photophoto.domain.view.ViewRepository;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import com.hjin.photophoto.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewService {

    private final ViewRepository viewRepository;
    private final PostsRepository postsRepository;


    @Transactional
    public void updateViewCount(Long userId) {
        View view = viewRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new MyException(MyExceptionType.NOT_EXIST_USER, userId)
                );

        Long countReadNot = postsRepository.countByReceiverUserIdAndReadYnNot(userId)
                .orElseThrow(
                        () -> new MyException(MyExceptionType.NOT_EXIST_USER, userId)
                );

        view.updateCount(Math.toIntExact(countReadNot));

    }

    @Transactional
    public View saveView(Long userId) {
//        System.out.println(">> userId: " + userId);
//        Optional<View> view = viewRepository.findByUserId(userId);
//        System.out.println(">> view: " + view);

        return viewRepository.save(View.builder()
                .userId(userId)
                .count(0)
                .build());


    }
}
