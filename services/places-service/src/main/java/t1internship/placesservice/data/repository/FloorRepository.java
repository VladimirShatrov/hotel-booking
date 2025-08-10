package t1internship.placesservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1internship.placesservice.data.entity.Floor;

import java.util.List;

@Repository
public interface FloorRepository extends JpaRepository<Floor,Long> {
    List<Floor>  findFloorsByLocationId(Long locationId);
}
