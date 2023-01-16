package com.hjin.photophoto.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FrameType {
    BROWN("BROWN", "갈색 프레임"),
    GOLD("GOLD", "금색 프레임"),
    BLUE("BLUE", "하늘색 프레임"),
    LAVENDER("LAVENDER", "보라색 프레임"),
    BLACK("BLACK", "검정색 프레임");

    private final String key;
    private final String title;
}
