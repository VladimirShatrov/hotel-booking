package t1internship.placesservice.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1internship.placesservice.api.dto.request.LocationRequest;
import t1internship.placesservice.api.dto.response.LocationResponse;
import t1internship.placesservice.api.mapper.LocationMapper;
import t1internship.placesservice.data.entity.Location;
import t1internship.placesservice.data.repository.LocationRepository;

import java.util.List;



@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = LocationMapper.toEntity(locationRequest);
        Location savedLocation = locationRepository.save(location);
        return LocationMapper.toResponse(savedLocation);
    }

    public List<LocationResponse> getAllLocationsWithFloorsAndSpaces() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
        .map(LocationMapper::toResponse)
        .toList();
    }
}
