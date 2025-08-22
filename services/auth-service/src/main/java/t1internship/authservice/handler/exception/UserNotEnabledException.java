package t1internship.authservice.handler.exception;

public class UserNotEnabledException extends RuntimeException {

    public UserNotEnabledException(String message) {
        super(message);
    }
}
