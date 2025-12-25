package org.relax.roomcatalogservice.dto;

import java.util.UUID;

public record RoomData(
        UUID id,
        String name,
        Integer capacity,
        String location
) {}