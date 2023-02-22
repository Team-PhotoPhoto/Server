package com.hjin.photophoto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    int getErrorCode();
    HttpStatus getHttpStatus();
    String getErrorMessage();
}
