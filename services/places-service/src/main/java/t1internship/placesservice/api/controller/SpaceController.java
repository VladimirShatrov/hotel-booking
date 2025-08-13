package t1internship.placesservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1internship.placesservice.api.dto.request.SpaceRequest;
import t1internship.placesservice.api.dto.response.SpaceResponse;
import t1internship.placesservice.api.service.SpaceService;

import java.util.List;

@RestController
@RequestMapping("api/v1/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping("/{id}")
    public ResponseEntity<SpaceResponse> createSpaceInFloorById(
            @Valid @RequestBody SpaceRequest spaceRequest,
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(spaceService.createSpace(spaceRequest,id));
    }

    @PostMapping("/many/{id}")
    public ResponseEntity<List<SpaceResponse>> createSpaceInFloorById(
            @Valid @RequestBody List<SpaceRequest> spaceRequests,
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(spaceService.createSpaces(spaceRequests,id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<SpaceResponse>> getSpacesByFloorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(spaceService.getSpacesByFloorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SpaceResponse> updateSpace(
            @PathVariable Long id,
            @Valid @RequestBody SpaceRequest spaceRequest) {
        return ResponseEntity.ok(spaceService.updateSpace(id, spaceRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpace(
            @PathVariable Long id) {
        spaceService.deleteSpace(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
