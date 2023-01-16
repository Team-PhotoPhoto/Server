package com.hjin.photophoto.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WallType {

    ZIGZAG("ZIGZAG", "지그재그 프레임"),
    SQUARE("SQUARE", "마름모 프레임"),
    STRIPE("STRIPE", "줄무늬 프레임"),
    FLOWER("FLOWER", "꽃 프레임"),
    LINE("LINE", "선형 프레임");

    private final String key;
    private final String title;
}
