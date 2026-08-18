package notification.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends MyException {

    public InvalidDataException(String message) {

        super(
                ErrorCode.INVALID_DATA,
                HttpStatus.BAD_REQUEST,
                message
        );
    }
}