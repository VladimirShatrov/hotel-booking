package t1internship.placesservice.api.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import t1internship.placesservice.api.dto.request.LocationRequest;
import t1internship.placesservice.api.dto.response.LocationResponse;
import t1internship.placesservice.api.exceptions.NotFoundLocationException;
import t1internship.placesservice.api.mapper.LocationMapper;
import t1internship.placesservice.data.entity.Location;
import t1internship.placesservice.data.repository.LocationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    @CachePut(value = "locations", key = "#result.id")
    public LocationResponse createLocation(LocationRequest locationRequest) {
        Location location = LocationMapper.toEntity(locationRequest);
        Location savedLocation = locationRepository.save(location);
        return LocationMapper.toResponse(savedLocation);
    }

    @Transactional
    @Cacheable(value = "locations", key = "'all_locations_' + #includeFloors + '_' + #includeSpaces")
    public List<LocationResponse> getLocations(boolean includeFloors, boolean includeSpaces) {
        List<Location> locations = locationRepository.findAll();
        if (includeFloors && includeSpaces) {
            return locations.stream()
                    .map(LocationMapper::toResponse)
                    .toList();
        } else if (includeFloors) {
            return locations.stream()
                    .map(LocationMapper::toResponseWithoutSpaces)
                    .toList();
        }
        return locations.stream()
                .map(LocationMapper::toResponseWithoutAll)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "locations", key = "#id")
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    @Transactional
    @CachePut(value = "locations", key = "#id")
    public LocationResponse updateLocation(Long id, LocationRequest locationRequest) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundLocationException(
                        String.format("Not found Location with id: %d", id)));
        LocationMapper.updateEntity(locationRequest, location);
        Location savedLocation = locationRepository.save(location);
        return LocationMapper.toResponse(savedLocation);
    }

    @Transactional
    @Cacheable(value = "locations", key = "#id")
    public LocationResponse getLocationById(Long id) {
        return LocationMapper.toResponse(
                locationRepository.findById(id)
                        .orElseThrow(() -> new NotFoundLocationException(
                                String.format("Not found Location with id: %d", id))));
    }

    @Transactional
    @CacheEvict(value = "locations", key = "'all_locations_' + #includeFloors + '_' + #includeSpaces")
    public List<LocationResponse> createLocations(@Valid List<LocationRequest> locationRequest) {
        List<Location> locations = locationRequest.stream()
                .map(LocationMapper::toEntity)
                .toList();
        List<Location> savedLocations = locationRepository.saveAll(locations);
        return savedLocations.stream()
                .map(LocationMapper::toResponseWithoutAll)
                .toList();
    }
}
