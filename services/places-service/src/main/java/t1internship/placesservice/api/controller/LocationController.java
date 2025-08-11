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

    @PostMapping()
    private ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocation(locationRequest));
    }

    @PostMapping("/many")
    private ResponseEntity<List<LocationResponse>> createLocations(
            @Valid @RequestBody List<LocationRequest> locationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocations(locationRequest));
    }

    @GetMapping
    private ResponseEntity<List<LocationResponse>> getAllLocationsWithFloorsAndSpaces(
            @RequestParam(defaultValue = "false") boolean includeFloors,
            @RequestParam(defaultValue = "false") boolean includeSpaces){
        return ResponseEntity.ok(locationService.getLocations(includeFloors,includeSpaces));
    }

    @GetMapping("/{id}")
    private ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id){
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteLocation(@PathVariable Long id){
        locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/{id}")
    private ResponseEntity<LocationResponse> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(locationService.updateLocation(id,locationRequest));
    }

}
