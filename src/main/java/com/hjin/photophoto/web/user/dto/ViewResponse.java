package com.hjin.photophoto.web.user.dto;

import com.hjin.photophoto.domain.view.View;
import com.hjin.photophoto.domain.view.ViewRepository;
import lombok.Getter;

@Getter
public class ViewResponse {
    private Long userId;
    private int count;

    public ViewResponse(View entity) {
        this.userId = entity.getUserId();
        this.count = 0;
    }
}
