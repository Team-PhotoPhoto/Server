package com.hjin.photophoto.service.view;


import com.hjin.photophoto.domain.view.View;
import com.hjin.photophoto.domain.view.ViewRepository;
import com.hjin.photophoto.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ViewService {

    private final ViewRepository viewRepository;


    @Transactional
    public void plus1ViewCount(Long userId) {
        View view = viewRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 유저가 없습니다. userId = " + userId
                ));

        view.updateCount(view.getCount() + 1);

    }

    @Transactional
    public void minus1ViewCount(Long userId) {
        View view = viewRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 유저가 없습니다. userId = " + userId
                ));

        view.updateCount(view.getCount() - 1);

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
