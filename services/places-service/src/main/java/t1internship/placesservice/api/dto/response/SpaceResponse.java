package t1internship.placesservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class SpaceResponse  implements Serializable {

    private Long id;
    private String name;
    private Integer seatingCapacity;
    private Long floorId;
    private String spaceType;
}
