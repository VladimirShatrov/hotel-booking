package org.relax.authservice.handler.exception;

public class PasswordDisMatchException extends RuntimeException {

    public PasswordDisMatchException(String message) {
        super(message);
    }
}
