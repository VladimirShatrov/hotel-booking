package t1internship.placesservice.data.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import t1internship.placesservice.data.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @EntityGraph(value = "Location.withFloors", type = EntityGraph.EntityGraphType.LOAD)
    List<Location> findAll();

    @EntityGraph(value = "Location.withFloors", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Location> findById(Long id);
}
