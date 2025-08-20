package t1internship.visualizationservice.api.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpaceDto {
    @NotNull(message = "placeId cannot be null")
    private Long spaceId;
    private int x;
    private int y;
}