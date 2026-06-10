package com.example.tracker.dto;

import com.example.tracker.exceptions.ErrorCode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {

    private LocalDateTime timestamp;

    private Integer status;

    private ErrorCode errorCode;

    private String message;
}