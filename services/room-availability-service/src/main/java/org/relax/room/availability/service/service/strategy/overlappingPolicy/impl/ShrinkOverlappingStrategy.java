package org.relax.room.availability.service.service.strategy.overlappingPolicy.impl;

import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//FIXME
public class ShrinkOverlappingStrategy implements OverlappingStrategy {


    @Override
    public RoomAvailability resolve(Room room, List<RoomAvailability> overlaps,
                                          RoomStatus newStatus, LocalDateTime startDate,
                                          LocalDateTime endDate) {
        List<RoomAvailability> modified = new ArrayList<>();

        for (RoomAvailability existing : overlaps) {
            LocalDateTime s = existing.getStartDate();
            LocalDateTime e = existing.getEndDate();

            if (startDate.isAfter(s) && endDate.isBefore(e)) {
                modified.add(RoomAvailability.builder()
                                .id(UUID.randomUUID())
                                .roomId(room)
                                .startDate(startDate.minusSeconds(1))
                                .status(existing.getStatus())
                        .build());
            }
        }

        return RoomAvailability.builder()
                .id(UUID.randomUUID())
                .roomId(room)
                .startDate(startDate)
                .endDate(endDate)
                .status(newStatus)
                .build();
    }
}
