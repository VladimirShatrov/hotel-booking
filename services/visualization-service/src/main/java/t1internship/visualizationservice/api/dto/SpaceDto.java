package t1internship.visualizationservice.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpaceDto {
    private Long placeId;
    private int x;
    private int y;
}