package org.relax.room.availability.service.service.strategy.changeStatus;

import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;

import java.time.LocalDateTime;

public interface RoomAvailabilityChangeStrategy {
    RoomAvailability apply(Room room,
                           LocalDateTime startDate,
                           LocalDateTime endDate,
                           OverlappingPolicy overlappingPolicy,
                           String reason
                           );

    RoomStatus getTargetStatus();
}
