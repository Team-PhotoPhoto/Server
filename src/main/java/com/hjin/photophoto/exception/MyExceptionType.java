package com.hjin.photophoto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MyExceptionType implements BaseExceptionType {
    NO_PERMISSION(600, HttpStatus.FORBIDDEN, "접근 권한이 없습니다. userId = "),
    NOT_EXIST_USER(601, HttpStatus.NOT_FOUND, "해당 유저가 없습니다. userId = "),
    NOT_EXIST_IMAGE_POST(602, HttpStatus.NOT_FOUND, "해당 게시글의 이미지가 없습니다. postId = "),
    NOT_EXIST_POST(603, HttpStatus.NOT_FOUND, "해당 게시글이 없습니다. postId = "),
    NOT_EXIST_BUCKET_TYPE(604, HttpStatus.NOT_FOUND, "잘못된 type 입력입니다. id = "),
    NOT_EXIST_VIEW(605, HttpStatus.NOT_FOUND, "해당 유저의 view가 없습니다. userId = ");
    private int errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;

    MyExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}
