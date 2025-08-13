package t1internship.placesservice.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import t1internship.placesservice.api.dto.request.SpaceRequest;
import t1internship.placesservice.api.dto.response.SpaceResponse;
import t1internship.placesservice.api.exceptions.NotFoundFloorException;
import t1internship.placesservice.api.exceptions.NotFoundSpaceException;
import t1internship.placesservice.api.mapper.SpaceMapper;
import t1internship.placesservice.data.entity.Floor;
import t1internship.placesservice.data.entity.Spaces.Space;
import t1internship.placesservice.data.repository.FloorRepository;
import t1internship.placesservice.data.repository.SpaceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final FloorRepository floorRepository;

    @Transactional
    @Cacheable(value = "spaces", key = "'floor_' + #floorId")
    public List<SpaceResponse> getSpacesByFloorId(Long floorId) {
        List<Space> spaces = spaceRepository.findByFloorId(floorId);
        return spaces.stream()
                .map(SpaceMapper::toResponse)
                .toList();
    }

    @Transactional
    @CachePut(value = "spaces", key = "#result.id")
    public SpaceResponse createSpace(SpaceRequest spaceRequest, Long id) {
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new NotFoundFloorException(
                        String.format("Not found Floor with id: %d", id)));
        Space space = SpaceMapper.toEntity(spaceRequest);
        floor.getSpaces().add(space);
        space.setFloor(floor);
        floorRepository.save(floor);
        return SpaceMapper.toResponse(space);
    }

    @Transactional
    @CacheEvict(value = "spaces", allEntries = true)
    public List<SpaceResponse> createSpaces(List<SpaceRequest> spaceRequests, Long floorId) {
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new NotFoundFloorException(
                        String.format("Not found Floor with id: %d", floorId)));
        List<Space> spaces = spaceRequests.stream()
                .map(SpaceMapper::toEntity)
                .toList();
        for (Space space : spaces) {
            floor.getSpaces().add(space);
            space.setFloor(floor);
        }
        floorRepository.save(floor);
        return spaces.stream()
                .map(SpaceMapper::toResponse)
                .toList();
    }

    @Transactional
    @CachePut(value = "spaces", key = "#id")
    public SpaceResponse updateSpace(Long id, SpaceRequest spaceRequest) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new NotFoundSpaceException(
                        String.format("Not found Space with id: %d", id)));
        Space updatedSpace = SpaceMapper.updateEntity(spaceRequest, space);
        Space savedSpace = spaceRepository.save(updatedSpace);
        return SpaceMapper.toResponse(savedSpace);
    }

    @Transactional
    @CacheEvict(value = "spaces", key = "#id")
    public void deleteSpace(Long id) {
        spaceRepository.deleteById(id);
    }

    @Transactional
    public Boolean checkExistenceOfSpace(Long id) {
        return spaceRepository.existsById(id);
    }
}