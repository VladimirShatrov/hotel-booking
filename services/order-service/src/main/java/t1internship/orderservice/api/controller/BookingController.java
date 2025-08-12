package t1internship.orderservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1internship.orderservice.api.dto.response.BookingResponse;
import t1internship.orderservice.api.dto.request.BookingRequest;
import t1internship.orderservice.api.service.OrderService;


@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class BookingController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<BookingResponse> createOrder(
            @Valid @RequestBody BookingRequest bookingRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(bookingRequest));
    }

    @GetMapping("/{id}")
    private ResponseEntity<BookingResponse> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<BookingResponse> getOrderByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }
}
