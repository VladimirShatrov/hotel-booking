package t1internship.placesservice.api.mapper;

import t1internship.placesservice.api.dto.request.LocationRequest;
import t1internship.placesservice.api.dto.response.FloorResponse;
import t1internship.placesservice.api.dto.response.LocationResponse;
import t1internship.placesservice.data.entity.Floor;
import t1internship.placesservice.data.entity.Location;

import java.util.List;
import java.util.stream.Collectors;

public class LocationMapper {

    public static Location toEntity(LocationRequest request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setStreet(request.getStreet());
        location.setBuildingNumber(request.getBuildingNumber());
        location.setPostalCode(request.getPostalCode());
        location.setPhoneNumber(request.getPhoneNumber());
        location.setEmail(request.getEmail());
        location.setCity(request.getCity());
        List<Floor> floors = request.getFloors().stream()
                .map(FloorMapper::toEntity)
                .collect(Collectors.toList());
        floors.forEach(floor -> floor.setLocation(location));
        location.setFloors(floors);
        return location;
    }

    public static LocationResponse toResponseWithoutSpaces(Location location) {
        LocationResponse response = getLocationResponse(location);
        List<FloorResponse> floorResponses = location.getFloors().stream()
                .map(FloorMapper::toResponseWithoutSpaces)
                .collect(Collectors.toList());
        response.setFloors(floorResponses);
        return response;
    }

    public static LocationResponse toResponseWithoutAll(Location location) {
        LocationResponse response = getLocationResponse(location);
        return response;
    }

    public static LocationResponse toResponse(Location location) {
        LocationResponse response = getLocationResponse(location);
        List<FloorResponse> floorResponses = location.getFloors().stream()
                .map(FloorMapper::toResponse)
                .collect(Collectors.toList());
        response.setFloors(floorResponses);
        return response;
    }


    public static Location updateEntity(LocationRequest request, Location location) {
        location.setName(request.getName());
        location.setStreet(request.getStreet());
        location.setBuildingNumber(request.getBuildingNumber());
        location.setPostalCode(request.getPostalCode());
        location.setPhoneNumber(request.getPhoneNumber());
        location.setEmail(request.getEmail());
        location.setCity(request.getCity());
        var floors = request.getFloors().stream()
                .map(floorRequest -> FloorMapper.updateEntity(floorRequest, new Floor()))
                .peek(floor -> floor.setLocation(location))
                .collect(Collectors.toList());
        location.getFloors().clear();
        location.getFloors().addAll(floors);
        return location;
    }


    private static LocationResponse getLocationResponse(Location location) {
        LocationResponse response = new LocationResponse();
        response.setId(location.getId());
        response.setName(location.getName());
        response.setStreet(location.getStreet());
        response.setBuildingNumber(location.getBuildingNumber());
        response.setPostalCode(location.getPostalCode());
        response.setPhoneNumber(location.getPhoneNumber());
        response.setEmail(location.getEmail());
        response.setCity(location.getCity());
        return response;
    }
}
