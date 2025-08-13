package t1internship.orderservice.api.dto.request;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderRequest {
    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "spaceId is required")
    private Long spaceId;

    @NotNull(message = "floorId is required")
    private Long floorId;

    @NotNull(message = "startTime is required")
    private LocalDateTime startTime;

    private String timeZone;

    @NotNull(message = "durationHours is required")
    @Min(value = 1, message = "durationHours must be at least 1")
    private Integer durationHours;
}
