package t1internship.visualizationservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloorResponseDto {
    private String imageUrl;
    private List<SpaceDto> places;
}