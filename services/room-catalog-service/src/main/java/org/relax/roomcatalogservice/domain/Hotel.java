package org.relax.roomcatalogservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.relax.roomcatalogservice.domain.geo.HotelLocation;

import java.util.UUID;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private HotelLocation location;
}
