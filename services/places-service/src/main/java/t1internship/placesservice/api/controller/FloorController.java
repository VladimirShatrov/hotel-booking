package t1internship.placesservice.api.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1internship.placesservice.api.dto.request.FloorRequest;
import t1internship.placesservice.api.dto.response.FloorResponse;
import t1internship.placesservice.api.service.FloorService;

import java.util.List;

@RestController
@RequestMapping("api/v1/floors")
@RequiredArgsConstructor
public class FloorController {

    private final FloorService floorService;

    @GetMapping("/{id}")
    public ResponseEntity<List<FloorResponse>> getFloorsByLocationId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") Boolean includeSpaces) {
        return ResponseEntity
                .ok(floorService.getFloorsByLocationId(id,includeSpaces));
    }

    @PostMapping("/{id}")
    public ResponseEntity<FloorResponse> createFloorInLocationById(
            @PathVariable Long id,
            @RequestBody FloorRequest floorRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(floorService.createFloor(floorRequest,id));
    }

    @PostMapping("/many/{id}")
    public ResponseEntity<List<FloorResponse>> createFloorsInLocationById(
            @PathVariable Long id,
            @Valid @RequestBody List<FloorRequest> floorRequests) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(floorService.createFloors(id,floorRequests));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long id){
        floorService.deleteFloor(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FloorResponse> updateFloor(
            @PathVariable Long id,
            @Valid @RequestBody FloorRequest floorRequest){
        return ResponseEntity.ok(floorService.updateFloor(id,floorRequest));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> checkSpaceExistence(@PathVariable Long id) {
        return floorService.checkExistenceOfSpace(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


}
