package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkSpaceRequest extends SpaceRequest {

    @NotNull(message = "Computer availability is mandatory")
    private Boolean hasComputer;

    @NotNull(message = "Number of sockets is mandatory")
    @PositiveOrZero(message = "Number of sockets must be zero or positive")
    private Integer numberOfSockets;

    @NotNull(message = "Number of monitors is mandatory")
    @PositiveOrZero(message = "Number of monitors must be zero or positive")
    private Integer numberOfMonitors;

    public WorkSpaceRequest(String name, Integer seatingCapacity, Long floorId,
                            Boolean hasComputer, Integer numberOfSockets, Integer numberOfMonitors) {
        super(name, seatingCapacity, floorId, "WORKSPACE");
        this.hasComputer = hasComputer;
        this.numberOfSockets = numberOfSockets;
        this.numberOfMonitors = numberOfMonitors;
    }
}
