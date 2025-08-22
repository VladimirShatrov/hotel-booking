package t1internship.authservice.handler.exception;

public class EmailNotValidException extends RuntimeException{

    public EmailNotValidException(String message) {
        super(message);
    }
}
