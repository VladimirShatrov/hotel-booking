package t1internship.placesservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorResponse  implements Serializable {

    private Long id;
    private Integer floorNumber;
    private Long locationId;
    private List<SpaceResponse> spaces = new ArrayList<>();
}
