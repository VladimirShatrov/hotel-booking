package t1internship.visualizationservice.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FloorVisualization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long floorId;

    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "floorVisualization" ,cascade = CascadeType.ALL)
    private List<SpaceCoordinates> spaceCoordinates;
}
