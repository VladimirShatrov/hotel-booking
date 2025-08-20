package t1internship.placesservice.api.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import t1internship.placesservice.api.dto.request.FloorRequest;
import t1internship.placesservice.api.dto.response.FloorResponse;
import t1internship.placesservice.api.exceptions.NotFoundFloorException;
import t1internship.placesservice.api.exceptions.NotFoundLocationException;
import t1internship.placesservice.api.mapper.FloorMapper;
import t1internship.placesservice.data.entity.Floor;
import t1internship.placesservice.data.entity.Location;
import t1internship.placesservice.data.repository.FloorRepository;
import t1internship.placesservice.data.repository.LocationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @Cacheable(value = "floors", key = "'location_' + #id + '_' + #includeSpaces")
    public List<FloorResponse> getFloorsByLocationId(Long id, Boolean includeSpaces) {
        List<Floor> floors = floorRepository.findFloorsByLocationId(id);
        if (includeSpaces) {
            return floors.stream()
                    .map(FloorMapper::toResponse)
                    .toList();
        } else {
            return floors.stream()
                    .map(FloorMapper::toResponseWithoutSpaces)
                    .toList();
        }
    }

    @Transactional
    @CachePut(value = "floors", key = "#result.id")
    public FloorResponse createFloor(FloorRequest floorRequest, Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundLocationException("Not found Location with id: " + id));
        Floor floor = FloorMapper.toEntity(floorRequest);
        floor.setLocation(location);
        floor = floorRepository.save(floor);
        FloorMapper.toEntity(floorRequest);
        location.addFloor(floor);
        locationRepository.save(location);
        return FloorMapper.toResponse(floor);
    }

    @Transactional
    @CachePut(value = "floors", key = "#id")
    public FloorResponse updateFloor(Long id, FloorRequest floorRequest) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new NotFoundFloorException(
                        String.format("Not found Floor with id: %d", id)));
        FloorMapper.updateEntity(floorRequest, floor);
        Floor savedFloor = floorRepository.save(floor);
        return FloorMapper.toResponse(savedFloor);
    }

    @Transactional
    @CacheEvict(value = "floors", key = "#id")
    public void deleteFloor(Long id) {
        floorRepository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "floors", allEntries = true)
    public List<FloorResponse> createFloors(Long id, @Valid List<FloorRequest> floorRequests) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundLocationException(
                        String.format("Not found Location with id: %d", id)));
        List<Floor> floors = floorRequests.stream()
                .map(FloorMapper::toEntity)
                .toList();
        for (Floor floor : floors) {
            floor.setLocation(location);
            floorRepository.save(floor);
            location.addFloor(floor);
        }
        locationRepository.save(location);
        return floors.stream()
                .map(FloorMapper::toResponse)
                .toList();
    }

    @Transactional
    public boolean checkExistenceOfSpace(Long id) {
        return floorRepository.existsById(id);
    }
}
