package t1internship.placesservice.data.entity.Spaces;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CHILL_SPACE")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chill_space", schema = "places_schema")
public class ChillSpace extends Space {

    private Integer numberOfSockets;

    public ChillSpace(String name, Integer seatingCapacity, Integer numberOfSockets) {
        super(name, seatingCapacity);
        this.numberOfSockets = numberOfSockets;
    }
}
