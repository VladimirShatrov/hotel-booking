package t1internship.placesservice.api.exceptions;

public class NotFoundSpaceException extends RuntimeException {
    public NotFoundSpaceException(String format) {
        super(format);
    }
}
