package com.example.analytics.exceptions;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends MyException {

    public AccessDeniedException(String message) {

        super(
                ErrorCode.ACCESS_DENIED,
                HttpStatus.FORBIDDEN,
                message
        );
    }
}