package t1internship.visualizationservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t1internship.visualizationservice.data.entity.FloorVisualization;

import java.util.Optional;

public interface FloorVisualizationRepository extends JpaRepository<FloorVisualization, Long> {
    Optional<FloorVisualizationRepository> findByFloorId(Long floorId);
}
