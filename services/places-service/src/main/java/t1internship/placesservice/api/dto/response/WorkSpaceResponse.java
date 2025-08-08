package t1internship.placesservice.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkSpaceResponse extends SpaceResponse {

    private Boolean hasComputer;
    private Integer numberOfSockets;
    private Integer numberOfMonitors;

    public WorkSpaceResponse(Long id, String name, Integer seatingCapacity, Long floorId,
                             Boolean hasComputer, Integer numberOfSockets, Integer numberOfMonitors) {
        super(id, name, seatingCapacity, floorId, "WORKSPACE");
        this.hasComputer = hasComputer;
        this.numberOfSockets = numberOfSockets;
        this.numberOfMonitors = numberOfMonitors;
    }
}
