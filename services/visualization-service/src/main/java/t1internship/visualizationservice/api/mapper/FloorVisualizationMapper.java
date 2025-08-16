package t1internship.visualizationservice.api.mapper;

import org.springframework.stereotype.Component;
import t1internship.visualizationservice.api.dto.FloorResponseDto;
import t1internship.visualizationservice.api.dto.SpaceDto;
import t1internship.visualizationservice.api.service.FloorVisualizationService;
import t1internship.visualizationservice.data.entity.FloorVisualization;
import t1internship.visualizationservice.data.entity.SpaceCoordinates;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FloorVisualizationMapper {

    public static FloorVisualization toFloorVisualization(Long floorId, String fileName, List<SpaceDto> spaces){
        FloorVisualization floorVisualization = new FloorVisualization();
        floorVisualization.setFloorId(floorId);
        floorVisualization.setImageUrl(fileName);
        List<SpaceCoordinates> coordinates = spaces.stream().map(p -> {
        SpaceCoordinates coord = new SpaceCoordinates();
        coord.setSpaceId(Long.valueOf(p.getPlaceId()));
        coord.setX(p.getX());
        coord.setY(p.getY());
        coord.setFloorvisualization(floorVisualization);
        return coord;
        }).collect(Collectors.toList());
        floorVisualization.setSpaceCoordinates(coordinates);
        return floorVisualization;
    }


}
