package org.relax.room.availability.service.service.strategy.changeStatus.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.port.out.RoomAvailabilityOutPort;
import org.relax.room.availability.service.service.strategy.changeStatus.RoomAvailabilityChangeStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.factory.OverlappingStrategyFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MaintenanceRoomStrategy implements RoomAvailabilityChangeStrategy {

    private final RoomAvailabilityOutPort availabilityOutPort;
    private final OverlappingStrategyFactory overlappingStrategyFactory;

    @Transactional
    @Override
    public RoomAvailability apply(Room room, LocalDateTime startDate, LocalDateTime endDate,
                                  OverlappingPolicy overlappingPolicy, String reason) {
        var overlapping = availabilityOutPort.findNestedAvailabilityIntervalsByRoom(room, startDate, endDate);
        boolean hasBookingOrBlocked = overlapping.stream()
                .anyMatch(a -> a.getStatus() == RoomStatus.BOOKED || a.getStatus() == RoomStatus.BLOCKED);

        if (hasBookingOrBlocked) {
            throw new IllegalStateException("Room is booked or blocked, cannot maintain during this period");
        }

        OverlappingStrategy overlappingStrategy = overlappingStrategyFactory.getStrategy(overlappingPolicy);
        var availability = overlappingStrategy.resolve(room, overlapping, RoomStatus.MAINTENANCE,
                startDate, endDate);

        return availabilityOutPort.save(availability);
    }

    @Override
    public RoomStatus getTargetStatus() {
        return RoomStatus.MAINTENANCE;
    }
}
