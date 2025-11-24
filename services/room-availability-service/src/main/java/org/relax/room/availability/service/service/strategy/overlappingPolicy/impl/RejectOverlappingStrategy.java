package org.relax.room.availability.service.service.strategy.overlappingPolicy.impl;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class RejectOverlappingStrategy implements OverlappingStrategy {

    @Transactional
    @Override
    public RoomAvailability resolve(Room room, List<RoomAvailability> overlaps,
                                          RoomStatus newStatus, LocalDateTime startDate,
                                          LocalDateTime endDate) {
        if (!overlaps.isEmpty()) {
            throw new PersistenceException(
                    String.format("Room %s already has overlapping availability intervals", room.getId())
            );
        }

        return RoomAvailability.builder()
                .id(UUID.randomUUID())
                .status(newStatus)
                .startDate(startDate)
                .endDate(endDate)
                .roomId(room)
                .build();
    }
}
