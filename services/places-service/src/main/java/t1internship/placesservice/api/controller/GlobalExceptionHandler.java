

package t1internship.placesservice.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import t1internship.placesservice.api.exceptions.NotFoundFloorException;
import t1internship.placesservice.api.exceptions.NotFoundLocationException;
import t1internship.placesservice.api.exceptions.NotFoundSpaceException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundLocationException.class, NotFoundFloorException.class, NotFoundSpaceException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
