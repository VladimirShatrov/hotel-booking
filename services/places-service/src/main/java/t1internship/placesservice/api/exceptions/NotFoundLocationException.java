package t1internship.placesservice.api.exceptions;

public class NotFoundLocationException extends RuntimeException {
    public NotFoundLocationException(String explanationOfException) {
        super(explanationOfException);
    }
}
