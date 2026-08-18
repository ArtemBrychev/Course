package notification.exceptions;

import org.springframework.http.HttpStatus;

public class DataAlreadyExistsException extends MyException {

    public DataAlreadyExistsException(String message) {

        super(
                ErrorCode.DATA_ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                message
        );
    }
}