package com.hjin.photophoto.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    // isOpened

    private int isOpened;

    @Builder
    public PostsUpdateRequestDto(int isOpened) {
        this.isOpened = isOpened;
    }
}
