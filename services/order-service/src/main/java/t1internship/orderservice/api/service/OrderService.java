package t1internship.orderservice.api.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import t1internship.orderservice.api.dto.request.BookingRequest;
import t1internship.orderservice.api.dto.response.BookingResponse;

@Service
public class OrderService {
    public BookingResponse createOrder(@Valid BookingRequest bookingRequest) {
    }

    public BookingResponse getOrderById(Long id) {
        return null;
    }

    public BookingResponse getOrderByUserId(Long userId) {
    }
}
