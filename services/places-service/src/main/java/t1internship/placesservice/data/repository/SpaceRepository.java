package t1internship.placesservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t1internship.placesservice.data.entity.Spaces.Space;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    List<Space> findByFloorId(Long floorId);
}