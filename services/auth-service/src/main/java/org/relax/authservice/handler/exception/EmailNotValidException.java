package org.relax.authservice.handler.exception;

public class EmailNotValidException extends RuntimeException{

    public EmailNotValidException(String message) {
        super(message);
    }
}
