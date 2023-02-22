package com.hjin.photophoto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionDto> handleBaseEx(BaseException exception){
        String errorMessage = exception.getExceptionType().getErrorMessage();
        int errorCode = exception.getExceptionType().getErrorCode();

        log.error("errorMessage(): {}",errorMessage);
        log.error("errorCode(): {}",errorCode);

        return new ResponseEntity<>(new ExceptionDto(errorCode, errorMessage),
                exception.getExceptionType().getHttpStatus());
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<HttpStatus> handleMemberEx(MyException exception){
        exception.printStackTrace();
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Data
    @AllArgsConstructor
    static class ExceptionDto {
        private int errorCode;
        private String errorMessage;
    }
}