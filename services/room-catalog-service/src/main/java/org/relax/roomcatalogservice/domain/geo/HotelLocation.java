package org.relax.roomcatalogservice.domain.geo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record HotelLocation(

        @Column(name = "latitude")
        Double latitude,

        @Column(name = "longitude")
        Double longitude

) {}