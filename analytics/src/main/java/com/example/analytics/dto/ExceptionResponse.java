package com.example.analytics.dto;

import com.example.analytics.exceptions.ErrorCode;
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