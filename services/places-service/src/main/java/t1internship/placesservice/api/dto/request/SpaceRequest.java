package t1internship.placesservice.api.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "spaceType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MeetingRoomRequest.class, name = "MEETING_ROOM"),
        @JsonSubTypes.Type(value = KitchenRequest.class, name = "KITCHEN"),
        @JsonSubTypes.Type(value = ChillSpaceRequest.class, name = "CHILL_SPACE"),
        @JsonSubTypes.Type(value = WorkSpaceRequest.class, name = "WORKSPACE")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class SpaceRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Seating capacity is mandatory")
    @PositiveOrZero(message = "Seating capacity must be zero or positive")
    private Integer seatingCapacity;

    @NotNull(message = "Floor ID is mandatory")
    private Long floorId;

    private String spaceType;
}
