package t1internship.orderservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import t1internship.orderservice.data.entity.OrderStatus;

@Data
public class UpdateBookingRequest {
    @NotNull(message = "status is required")
    private OrderStatus status;
}