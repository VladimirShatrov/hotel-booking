package t1internship.placesservice.api.dto.response;

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
public class LocationResponse {

    private Long id;
    private String name;
    private String street;
    private String buildingNumber;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String city;
    private List<FloorResponse> floors = new ArrayList<>();
}