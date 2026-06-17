package com.example.analytics.exceptions;

import com.example.analytics.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MyException.class)
    public ResponseEntity<ExceptionResponse> handleMyException( MyException ex) {
        ExceptionResponse response =
                ExceptionResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(ex.getHttpStatus().value())
                        .errorCode(ex.getErrorCode())
                        .message(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException( Exception ex ) {
        MyException myException = findMyException(ex);

        if (myException != null) {
            return handleMyException(myException);
        }

        log.error(
                "Internal error: {}",
                ex.getMessage(),
                ex
        );

        ExceptionResponse response =
                ExceptionResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(500)
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage())
                        .build();

        return ResponseEntity
                .internalServerError()
                .body(response);
    }

    private MyException findMyException( Exception ex ) {
        Throwable current = ex;
        while (current != null) {

            if (current instanceof MyException myEx) {
                return myEx;
            }

            current = current.getCause();
        }

        return null;
    }

}