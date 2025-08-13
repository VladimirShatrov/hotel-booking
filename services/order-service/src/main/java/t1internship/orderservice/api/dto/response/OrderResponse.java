package t1internship.orderservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import t1internship.orderservice.data.entity.OrderStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private Long spaceId;
    private Long floorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
