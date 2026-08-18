package notification.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MyException {

    public UserNotFoundException(String message) {

        super(
                ErrorCode.USER_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                message
        );
    }
}