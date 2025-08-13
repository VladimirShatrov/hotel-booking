package t1internship.orderservice.api.mapper;

import org.springframework.stereotype.Component;
import t1internship.orderservice.api.dto.request.OrderRequest;
import t1internship.orderservice.api.dto.request.UpdateBookingRequest;
import t1internship.orderservice.api.dto.response.OrderResponse;
import t1internship.orderservice.data.entity.Order;
import t1internship.orderservice.data.entity.OrderStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class OrderMapper {
    public static Order toEntity(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setSpaceId(orderRequest.getSpaceId());
        order.setFloorId(orderRequest.getFloorId());
        String timeZone = orderRequest.getTimeZone() != null ? orderRequest.getTimeZone() : "UTC";
        ZonedDateTime startTimeZoned = orderRequest.getStartTime().atZone(ZoneId.of(timeZone));
        order.setStartTime(startTimeZoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        order.setEndTime(startTimeZoned.plusHours(orderRequest.getDurationHours()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        order.setStatus(OrderStatus.CONFIRMED);
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        OrderResponse orderResponse;
        orderResponse = new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getSpaceId(),
                order.getFloorId(),
                order.getStartTime(),
                order.getEndTime(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt());
        return orderResponse;
    }

    public static void updateEntity(Order order, UpdateBookingRequest updateRequest) {
        if (order == null || updateRequest == null) {
            throw new IllegalArgumentException("Booking or UpdateBookingRequest cannot be null");
        }
        order.setStatus(OrderStatus.valueOf(updateRequest.getStatus().name()));
        order.setUpdatedAt(LocalDateTime.now());
    }
}
