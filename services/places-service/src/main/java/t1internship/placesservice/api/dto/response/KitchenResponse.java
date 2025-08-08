package t1internship.placesservice.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KitchenResponse extends SpaceResponse {

    private Boolean hasCoffeeMachine;
    private Boolean hasMicrowave;
    private Boolean hasFridge;

    public KitchenResponse(Long id, String name, Integer seatingCapacity, Long floorId,
                           Boolean hasCoffeeMachine, Boolean hasMicrowave, Boolean hasFridge) {
        super(id, name, seatingCapacity, floorId, "KITCHEN");
        this.hasCoffeeMachine = hasCoffeeMachine;
        this.hasMicrowave = hasMicrowave;
        this.hasFridge = hasFridge;
    }
}
