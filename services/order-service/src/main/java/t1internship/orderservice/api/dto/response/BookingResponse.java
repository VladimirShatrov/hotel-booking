package t1internship.orderservice.api.dto.response;

import lombok.Data;

import lombok.Data;
import t1internship.orderservice.data.entity.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long spaceId;
    private Long floorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
