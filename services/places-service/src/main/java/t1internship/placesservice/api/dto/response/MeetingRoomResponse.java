package t1internship.placesservice.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MeetingRoomResponse extends SpaceResponse  implements Serializable {

    private Boolean hasProjector;
    private Boolean hasDrawingBoard;

    public MeetingRoomResponse(Long id, String name, Integer seatingCapacity, Long floorId,
                               Boolean hasProjector, Boolean hasDrawingBoard) {
        super(id, name, seatingCapacity, floorId, "MEETING_ROOM");
        this.hasProjector = hasProjector;
        this.hasDrawingBoard = hasDrawingBoard;
    }
}