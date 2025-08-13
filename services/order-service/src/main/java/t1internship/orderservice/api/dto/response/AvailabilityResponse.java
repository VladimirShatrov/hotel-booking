package t1internship.orderservice.api.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvailabilityResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}