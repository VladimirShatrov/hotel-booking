package org.relax.room.availability.service.service.strategy.overlappingPolicy;

import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OverlappingStrategy {

    RoomAvailability resolve(
            final Room room,
            final List<RoomAvailability> overlaps,
            final RoomStatus newStatus,
            final LocalDateTime startDate,
            final LocalDateTime endDate
    );
}
