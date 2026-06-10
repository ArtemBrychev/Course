package com.example.tracker.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MyException extends RuntimeException {

    private final ErrorCode errorCode;

    private final HttpStatus httpStatus;

    public MyException(
            ErrorCode errorCode,
            HttpStatus httpStatus,
            String message
    ) {

        super(message);

        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}