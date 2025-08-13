package t1internship.orderservice.api.exception;

public class OrderNotFreeException extends RuntimeException {
    public OrderNotFreeException(String s) {
        super(s);
    }
}
