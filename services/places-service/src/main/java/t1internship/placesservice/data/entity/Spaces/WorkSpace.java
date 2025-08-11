package t1internship.placesservice.data.entity.Spaces;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("WORKSPACE")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "work_space", schema = "places_schema")
public class WorkSpace extends Space {
    private Boolean hasComputer;
    private Integer numberOfSockets;
    private Integer numberOfMonitors;

    public WorkSpace(String name, Integer seatingCapacity, Boolean hasComputer,
                     Integer numberOfSockets, Integer numberOfMonitors) {
        super(name, seatingCapacity);
        this.hasComputer = hasComputer;
        this.numberOfSockets = numberOfSockets;
        this.numberOfMonitors = numberOfMonitors;
    }
}
