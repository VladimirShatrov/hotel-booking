package org.relax.room.availability.service.dto;

import org.relax.room.availability.service.domain.RoomStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoomAvailabilityData(
        UUID id,
        UUID roomId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        RoomStatus status
) {
}
