package t1internship.orderservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1internship.orderservice.api.dto.request.UpdateBookingRequest;
import t1internship.orderservice.api.dto.response.OrderResponse;
import t1internship.orderservice.api.dto.request.OrderRequest;
import t1internship.orderservice.api.service.OrderService;

import java.util.List;


@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrderById(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateBookingRequest updateRequest) {
        return ResponseEntity.ok(orderService.updateOrderById(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "false") Boolean includeConfirmed,
            @RequestParam(defaultValue = "false") Boolean includeCanceled,
            @RequestParam(defaultValue = "false") Boolean includeCompleted) {
        return ResponseEntity.ok(orderService.getAllOrders(
                includeConfirmed, includeCanceled, includeCompleted));
    }

}
