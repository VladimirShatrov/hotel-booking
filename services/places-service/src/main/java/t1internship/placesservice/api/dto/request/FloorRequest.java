package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorRequest {

    @NotNull(message = "Floor number is mandatory")
    @Positive(message = "Floor number must be positive")
    private Integer floorNumber;

    @NotNull(message = "Location ID is mandatory")
    private Long locationId;

    private List<SpaceRequest> spaces = new ArrayList<>();
}
