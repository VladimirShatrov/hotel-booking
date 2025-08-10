package t1internship.placesservice.api.exceptions;

public class NotFoundFloorException extends RuntimeException {
    public NotFoundFloorException(String errorMessage) {
        super(errorMessage);
    }
}
