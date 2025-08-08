package t1internship.placesservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1internship.placesservice.api.dto.request.LocationRequest;
import t1internship.placesservice.api.dto.response.LocationResponse;
import t1internship.placesservice.api.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    private ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocation(locationRequest));
    }

    @GetMapping
    private ResponseEntity<List<LocationResponse>> getAllLocationsWithFloorsAndSpaces(){
        return ResponseEntity.ok(locationService.getAllLocationsWithFloorsAndSpaces());
    }
}
