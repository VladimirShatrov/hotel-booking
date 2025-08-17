package t1internship.visualizationservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import t1internship.visualizationservice.api.dto.FloorResponseDto;
import t1internship.visualizationservice.api.dto.SpaceDto;
import t1internship.visualizationservice.api.service.FloorVisualizationService;
import t1internship.visualizationservice.data.entity.FloorVisualization;

import java.util.List;

@RestController
@RequestMapping("/api/v1/floors")
@RequiredArgsConstructor
public class FloorVisualizationController {

    private final FloorVisualizationService floorService;

    @PostMapping(value ="/{floorId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFloor(
            @PathVariable Long floorId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("places") List<SpaceDto> places) {
        try {
            String fileName = floorService.uploadFloor(floorId, file, places);
            return ResponseEntity.ok("Floor uploaded successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{floorId}")
    public ResponseEntity<FloorResponseDto> getFloor(@PathVariable Long floorId) {
        try {
            FloorResponseDto response = floorService.getFloor(floorId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
