package t1internship.placesservice.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ChillSpaceResponse extends SpaceResponse  implements Serializable {

    private Integer numberOfSockets;

    public ChillSpaceResponse(Long id, String name, Integer seatingCapacity, Long floorId,
                              Integer numberOfSockets) {
        super(id, name, seatingCapacity, floorId, "CHILL_SPACE");
        this.numberOfSockets = numberOfSockets;
    }
}