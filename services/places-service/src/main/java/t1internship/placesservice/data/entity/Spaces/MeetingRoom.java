package t1internship.placesservice.data.entity.Spaces;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("MEETING_ROOM")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "meeting_room")
public class MeetingRoom extends Space {
    private Boolean hasProjector;
    private Boolean hasDrawingBoard;

    public MeetingRoom(String name, Integer seatingCapacity, Boolean hasProjector, Boolean hasDrawingBoard) {
        super(name, seatingCapacity);
        this.hasProjector = hasProjector;
        this.hasDrawingBoard = hasDrawingBoard;
    }
}
