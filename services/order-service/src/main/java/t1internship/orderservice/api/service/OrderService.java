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
import t1internship.orderservice.data.repository.OrderRepositoryImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final OrderRepositoryImpl orderRepositoryImpl;

    @Value("${places-service.base-url}")
    private String placesServiceBaseUrl;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        checkSpaceExists(orderRequest.getSpaceId());
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

    private void checkSpaceExists(Long spaceId) {
        String url = placesServiceBaseUrl  + spaceId + "/exists";
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Space with id " + spaceId + " does not exist");
            }
            System.out.println("ok");
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Space with id " + spaceId + " does not exist");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IllegalArgumentException("Space with id " + spaceId + " does not exist");
            }
            throw new RuntimeException("Failed to check space existence: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to check space existence: " + e.getMessage());
        }
    }

    private void checkUserExists(UUID userId) {
        String url = userServiceBaseUrl  + userId + "/exists";
        try {
            ResponseEntity<Void> response = restTemplate.getForEntity(url, Void.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("User with id " + userId + " does not exist");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("User with id " + userId + " does not exist");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IllegalArgumentException("User with id " + userId + " does not exist");
            }
            throw new RuntimeException("Failed to check user existence: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to check user existence: " + e.getMessage());
        }
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
    public List<OrderResponse> getOrdersByUserId(UUID userId) {
        List<Order> orders = orderRepository
                .findOrdersByUserId(userId);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> getOrdersByFloorId(Long floorId) {
        List<Order> orders = orderRepository.findOrdersByFloorId(floorId);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponse> filterOrders(
            Long floorId, Long spaceId, LocalDateTime startTime,
                                            LocalDateTime endTime, List<OrderStatus> statuses) {
        List<Order> orders = orderRepositoryImpl.findOrdersByCriteria(floorId, spaceId, startTime, endTime, statuses);
        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
