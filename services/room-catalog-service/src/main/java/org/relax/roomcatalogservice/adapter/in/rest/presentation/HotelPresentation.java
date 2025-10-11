package org.relax.roomcatalogservice.adapter.in.rest.presentation;

import java.util.UUID;

public record HotelPresentation(
        UUID id,
        String name,
        Double latitude,
        Double longitude
) {
    public static HotelPresentation from(org.relax.roomcatalogservice.domain.Hotel h) {
        if (h == null) return null;
        return new HotelPresentation(h.getId(), h.getName(),
                h.getLocation() != null ? h.getLocation().latitude() : null,
                h.getLocation() != null ? h.getLocation().longitude() : null);
    }
}
