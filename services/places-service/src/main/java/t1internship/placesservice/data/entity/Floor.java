package t1internship.placesservice.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import t1internship.placesservice.data.entity.Spaces.Space;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "floor", schema = "places_schema")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="floor_number", nullable = false)
    private Integer floorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    @BatchSize(size = 50)
    private List<Space> spaces = new ArrayList<>();

    public void addSpace(Space space) {
        spaces.add(space);
        space.setFloor(this);
    }

    public void removeSpace(Space space) {
        spaces.remove(space);
        space.setFloor(null);
    }
}