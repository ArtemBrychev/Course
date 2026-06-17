package com.example.analytics.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends MyException {

    public DataNotFoundException(String message) {

        super(
                ErrorCode.DATA_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                message
        );
    }
}