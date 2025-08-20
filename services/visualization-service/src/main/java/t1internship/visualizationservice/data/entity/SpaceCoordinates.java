package t1internship.visualizationservice.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Proxy(lazy = false)
public class SpaceCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long spaceId;

    private int x;
    private int y;

    @ManyToOne
    @JoinColumn(name = "floor_image_id")
    private FloorVisualization floorVisualization;
}
