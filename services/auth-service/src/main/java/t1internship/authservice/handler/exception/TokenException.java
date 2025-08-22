package t1internship.authservice.handler.exception;

public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
