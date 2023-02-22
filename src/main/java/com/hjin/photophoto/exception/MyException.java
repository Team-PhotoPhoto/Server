package com.hjin.photophoto.exception;

public class MyException extends BaseException {
    private final BaseExceptionType exceptionType;
    private final Long id;

    public MyException(BaseExceptionType exceptionType, Long id) {
        this.exceptionType = exceptionType;
        this.id = id;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
