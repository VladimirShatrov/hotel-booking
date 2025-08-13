package t1internship.placesservice.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class LocationRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Street is mandatory")
    @Size(max = 100, message = "Street must not exceed 100 characters")
    private String street;

    @NotBlank(message = "Building number is mandatory")
    @Size(max = 10, message = "Building number must not exceed 10 characters")
    private String buildingNumber;

    @Pattern(regexp = "\\d{5}", message = "Postal code must be exactly 5 digits")
    private String postalCode;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be valid (10-15 digits, optional +)")
    private String phoneNumber;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "City is mandatory")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    private List<FloorRequest> floors = new ArrayList<>();
}