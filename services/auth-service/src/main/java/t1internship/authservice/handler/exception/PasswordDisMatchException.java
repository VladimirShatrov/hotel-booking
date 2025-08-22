package t1internship.authservice.handler.exception;

public class PasswordDisMatchException extends RuntimeException {

    public PasswordDisMatchException(String message) {
        super(message);
    }
}
