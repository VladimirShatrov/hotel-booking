package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChillSpaceRequest extends SpaceRequest {

    @NotNull(message = "Number of sockets is mandatory")
    @PositiveOrZero(message = "Number of sockets must be zero or positive")
    private Integer numberOfSockets;

    public ChillSpaceRequest(String name, Integer seatingCapacity, Long floorId,
                             Integer numberOfSockets) {
        super(name, seatingCapacity, floorId, "CHILL_SPACE");
        this.numberOfSockets = numberOfSockets;
    }
}
