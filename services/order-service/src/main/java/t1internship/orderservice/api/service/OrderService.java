package t1internship.orderservice.api.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import t1internship.orderservice.api.dto.request.OrderRequest;
import t1internship.orderservice.api.dto.request.UpdateBookingRequest;
import t1internship.orderservice.api.dto.response.OrderResponse;
import t1internship.orderservice.api.exception.OrderNotFreeException;
import t1internship.orderservice.api.mapper.OrderMapper;
import t1internship.orderservice.data.entity.Order;
import t1internship.orderservice.data.entity.OrderStatus;
import t1internship.orderservice.data.repository.OrderRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${places-service.base-url}")
    private String placesServiceBaseUrl;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        String url1 = placesServiceBaseUrl + orderRequest.getSpaceId() + "/exists";
        String url2 = userServiceBaseUrl + orderRequest.getUserId() + "/exists";
        try {
            ResponseEntity<Void> response1 = restTemplate.getForEntity(url1, Void.class);
            ResponseEntity<Void> response2 = restTemplate.getForEntity(url2, Void.class);
            if (!response1.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Space with id " + orderRequest.getSpaceId() + " does not exist");
            }
            if (!response2.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("User with id " + orderRequest.getUserId() + " does not exist");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IllegalArgumentException("Space with id " + orderRequest.getSpaceId() + " does not exist");
            }
            throw new RuntimeException("Failed to check space existence: " + e.getMessage());
        }
        String timeZone = orderRequest.getTimeZone() != null ? orderRequest.getTimeZone() : "UTC";
        LocalDateTime startTimeUtc = orderRequest.getStartTime().atZone(ZoneId.of(timeZone))
                .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime endTimeUtc = startTimeUtc.plusHours(orderRequest.getDurationHours());
        List<Order> orders = orderRepository.findOrdersBySpaceIdAndTimeRangeAndStatus(
                orderRequest.getSpaceId(),
                startTimeUtc,
                endTimeUtc,
                OrderStatus.CONFIRMED
        );
        if (!orders.isEmpty()) {
            throw new OrderNotFreeException("Space with id: "+ orderRequest.getSpaceId()+" is busy");
        }
        Order order = OrderMapper.toEntity(orderRequest);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponse(savedOrder);
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateExpiredOrders() {
        List<Order> expiredOrders = orderRepository.findConfirmedOrdersWithExpiredEndTime(LocalDateTime.now(ZoneOffset.UTC));
         expiredOrders.forEach(order ->
                     order.setStatus(OrderStatus.COMPLETED));
        orderRepository.saveAll(expiredOrders);
    }

    @Transactional
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderById(Long id, @Valid UpdateBookingRequest updateRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        OrderMapper.updateEntity(order, updateRequest);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }

    @Transactional
    public List<OrderResponse> getAllOrders(
            Boolean includeConfirmed,
            Boolean includeCanceled,
            Boolean includeCompleted) {
        List<String> statuses = new ArrayList<>();
        if (includeConfirmed) statuses.add(OrderStatus.CONFIRMED.name());
        if (includeCanceled) statuses.add(OrderStatus.CANCELLED.name());
        if (includeCompleted) statuses.add(OrderStatus.COMPLETED.name());
        if (statuses.isEmpty()) {
            return Collections.emptyList();
        }
        List<Order> orders = orderRepository.findByStatusIn(statuses);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository
                .findOrdersByUserId(userId);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
