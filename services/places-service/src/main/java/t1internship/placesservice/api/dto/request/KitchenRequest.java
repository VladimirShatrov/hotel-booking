package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KitchenRequest extends SpaceRequest {

    @NotNull(message = "Coffee machine availability is mandatory")
    private Boolean hasCoffeeMachine;

    @NotNull(message = "Microwave availability is mandatory")
    private Boolean hasMicrowave;

    @NotNull(message = "Fridge availability is mandatory")
    private Boolean hasFridge;

    public KitchenRequest(String name, Integer seatingCapacity, Long floorId,
                          Boolean hasCoffeeMachine, Boolean hasMicrowave, Boolean hasFridge) {
        super(name, seatingCapacity, floorId, "KITCHEN");
        this.hasCoffeeMachine = hasCoffeeMachine;
        this.hasMicrowave = hasMicrowave;
        this.hasFridge = hasFridge;
    }
}