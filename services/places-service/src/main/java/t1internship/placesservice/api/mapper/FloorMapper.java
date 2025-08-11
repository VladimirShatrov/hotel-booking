package t1internship.placesservice.api.mapper;



import t1internship.placesservice.api.dto.request.FloorRequest;
import t1internship.placesservice.api.dto.response.FloorResponse;
import t1internship.placesservice.api.dto.response.SpaceResponse;
import t1internship.placesservice.data.entity.Floor;
import t1internship.placesservice.data.entity.Spaces.Space;

import java.util.List;
import java.util.stream.Collectors;

public class FloorMapper {

    public static Floor toEntity(FloorRequest request) {
        Floor floor = new Floor();
        floor.setFloorNumber(request.getFloorNumber());
        List<Space> spaces = request.getSpaces().stream()
                .map(SpaceMapper::toEntity)
                .collect(Collectors.toList());
        spaces.forEach(space -> space.setFloor(floor));
        floor.setSpaces(spaces);
        return floor;
    }

    public static FloorResponse toResponse(Floor floor) {
        FloorResponse response = new FloorResponse();
        response.setId(floor.getId());
        response.setFloorNumber(floor.getFloorNumber());
        response.setLocationId(floor.getLocation().getId());
        List<SpaceResponse> spaceResponses = floor.getSpaces().stream()
                .map(SpaceMapper::toResponse)
                .collect(Collectors.toList());
        response.setSpaces(spaceResponses);
        return response;
    }

    public static FloorResponse toResponseWithoutSpaces(Floor floor) {
        FloorResponse response = new FloorResponse();
        response.setId(floor.getId());
        response.setFloorNumber(floor.getFloorNumber());
        response.setLocationId(floor.getLocation() != null ? floor.getLocation().getId() : null);
        return response;
    }

    public static Floor updateEntity(FloorRequest request, Floor floor) {
        floor.setFloorNumber(request.getFloorNumber());

        var spaces = request.getSpaces().stream()
                .map(SpaceMapper::toEntity)
                .peek(space -> space.setFloor(floor))
                .toList();
        floor.getSpaces().clear();
        floor.getSpaces().addAll(spaces);

        return floor;
    }
}
