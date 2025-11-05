package org.relax.room.availability.service.service.strategy.changeStatus.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BlockRoomStrategy implements RoomAvailabilityChangeStrategy {

    private final RoomAvailabilityOutPort availabilityOutPort;
    private final OverlappingStrategyFactory overlappingStrategyFactory;

    @Override
    public RoomAvailability apply(Room room, LocalDateTime startDate,
                                  LocalDateTime endDate, OverlappingPolicy overlappingPolicy,
                                  String reason) {

        var overlapping = availabilityOutPort.findNestedAvailabilityIntervalsByRoom(room, startDate, endDate);
        boolean hasBooking = overlapping.stream()
                .anyMatch(a -> a.getStatus() == RoomStatus.BOOKED);

        if (hasBooking) {
            throw new IllegalStateException("Room is booked, cannot block during this period");
        }

        OverlappingStrategy overlappingStrategy = overlappingStrategyFactory.getStrategy(overlappingPolicy);

        return overlappingStrategy.resolve(room, overlapping, RoomStatus.BLOCKED,
                startDate, endDate);
    }

    @Override
    public RoomStatus getTargetStatus() {
        return RoomStatus.BLOCKED;
    }
}
