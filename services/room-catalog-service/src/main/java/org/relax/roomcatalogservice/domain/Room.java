package org.relax.roomcatalogservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
public class Room {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String number;

    @Column
    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Convert(converter = org.relax.roomcatalogservice.domain.converter.SetStringJsonConverter.class)
    @Column(columnDefinition = "jsonb")
    private Set<String> amenities;
}
