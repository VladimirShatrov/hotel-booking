package org.relax.roomcatalogservice.adapter.in.rest.presentation;

import java.util.Set;
import java.util.UUID;

public record RoomPresentation(
        UUID id,
        String number,
        Integer capacity,
        HotelPresentation hotel,
        Set<String> amenities
) {
    public static RoomPresentation from(org.relax.roomcatalogservice.domain.Room r) {
        if (r == null) return null;
        return new RoomPresentation(
                r.getId(),
                r.getNumber(),
                r.getCapacity(),
                HotelPresentation.from(r.getHotel()),
                r.getAmenities()
        );
    }
}
