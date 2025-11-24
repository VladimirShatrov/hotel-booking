package org.relax.room.availability.service.adapter.in.rest.presentaion;

import org.relax.room.availability.service.domain.RoomStatus;

import java.time.LocalDateTime;

public record RoomAvailabilityPresentationV1(

        String id,
        String roomId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        RoomStatus status
) {
}
