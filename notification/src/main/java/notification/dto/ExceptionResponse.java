package notification.dto;

import lombok.Builder;
import lombok.Data;
import notification.exceptions.ErrorCode;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {

    private LocalDateTime timestamp;

    private Integer status;

    private ErrorCode errorCode;

    private String message;
}