package t1internship.placesservice.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location", schema = "places_schema")
@NamedEntityGraph(
        name = "Location.withFloors",
        attributeNodes = {
                @NamedAttributeNode("floors")
        }
)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String street;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "postal_code", length = 5)
    private String postalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String city;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Floor> floors = new ArrayList<>();

    public void addFloor(Floor floor) {
        floors.add(floor);
        floor.setLocation(this);
    }

    public void removeFloor(Floor floor) {
        floors.remove(floor);
        floor.setLocation(null);
    }
}
