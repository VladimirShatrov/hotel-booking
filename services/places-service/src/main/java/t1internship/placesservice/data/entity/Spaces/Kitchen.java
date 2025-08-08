package t1internship.placesservice.data.entity.Spaces;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("KITCHEN")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "kitchen")
public class Kitchen extends Space {

    private Boolean hasCoffeeMachine;
    private Boolean hasMicrowave;
    private Boolean hasFridge;

    public Kitchen(String name, Integer seatingCapacity,
                   Boolean hasCoffeeMachine, Boolean hasMicrowave, Boolean hasFridge) {
        super(name, seatingCapacity);
        this.hasCoffeeMachine = hasCoffeeMachine;
        this.hasMicrowave = hasMicrowave;
        this.hasFridge = hasFridge;
    }
}
