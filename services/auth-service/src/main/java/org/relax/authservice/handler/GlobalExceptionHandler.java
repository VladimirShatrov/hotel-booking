package org.relax.authservice.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.relax.authservice.handler.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
            UsernameNotFoundException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotValidException(
            EmailNotValidException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(PasswordDisMatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordDisMatchException(
            PasswordDisMatchException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(RoleException.class)
    public ResponseEntity<ErrorResponse> handleRoleException(
            RoleException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(
            TokenException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), URI.create(request.getRequestURI()));
    }

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<ErrorResponse> handleUserNotEnabledException(
            UserNotEnabledException e,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.LOCKED, e.getMessage(), URI.create(request.getRequestURI()));
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, URI uri) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                status.value(),
                error,
                uri.getPath()
        );
        return new ResponseEntity<>(body, status);
    }
}
