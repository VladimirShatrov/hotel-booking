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

    @PostMapping("/admin")
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocation(locationRequest));
    }

    @PostMapping("admin/many")
    public ResponseEntity<List<LocationResponse>> createLocations(
            @Valid @RequestBody List<LocationRequest> locationRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationService.createLocations(locationRequest));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocationsWithFloorsAndSpaces(
            @RequestParam(defaultValue = "false") boolean includeFloors,
            @RequestParam(defaultValue = "false") boolean includeSpaces){
        return ResponseEntity.ok(locationService.getLocations(includeFloors,includeSpaces));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id){
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id){
        locationService.deleteLocation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(locationService.updateLocation(id,locationRequest));
    }

}
