package t1internship.visualizationservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import t1internship.visualizationservice.api.dto.FloorResponseDto;
import t1internship.visualizationservice.api.dto.SpaceDto;
import t1internship.visualizationservice.api.service.FloorVisualizationService;
import t1internship.visualizationservice.data.entity.FloorVisualization;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
public class FloorVisualizationController {

    private final FloorVisualizationService floorService;

    @PostMapping("/{floorId}/upload")
    public ResponseEntity<String> uploadFloor(
            @PathVariable Long floorId,
            @RequestParam("file") MultipartFile file,
            @RequestBody List<SpaceDto> places) {
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
