package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingRoomRequest extends SpaceRequest {

    @NotNull(message = "Projector availability is mandatory")
    private Boolean hasProjector;

    @NotNull(message = "Drawing board availability is mandatory")
    private Boolean hasDrawingBoard;

    public MeetingRoomRequest(String name, Integer seatingCapacity, Long floorId,
                              Boolean hasProjector, Boolean hasDrawingBoard) {
        super(name, seatingCapacity, floorId, "MEETING_ROOM");
        this.hasProjector = hasProjector;
        this.hasDrawingBoard = hasDrawingBoard;
    }
}