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
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookRoomStrategy implements RoomAvailabilityChangeStrategy {

    private final RoomAvailabilityOutPort availabilityOutPort;
    private final OverlappingStrategyFactory overlappingStrategyFactory;

    @Transactional
    @Override
    public RoomAvailability apply(Room room, LocalDateTime startDate, LocalDateTime endDate, OverlappingPolicy overlappingPolicy, String reason) {

        if (overlappingPolicy != OverlappingPolicy.REJECT) {
            throw new IllegalArgumentException("Booking allows only REJECT overlapping policy");
        }

        List<RoomAvailability> overlaps = availabilityOutPort.findNestedAvailabilityIntervalsByRoom(room, startDate, endDate.plusHours(6));

        OverlappingStrategy overlappingStrategy = overlappingStrategyFactory.getStrategy(overlappingPolicy);
        var roomAvailability =  overlappingStrategy.resolve(room, overlaps, RoomStatus.BOOKED, startDate, endDate);

        var maintainRoom = RoomAvailability.builder()
                .id(UUID.randomUUID())
                .roomId(room)
                .status(RoomStatus.MAINTENANCE)
                .startDate(endDate)
                .endDate(endDate.plusHours(6))
                .build();

        availabilityOutPort.save(maintainRoom);

        return availabilityOutPort.save(roomAvailability);
    }

    @Override
    public RoomStatus getTargetStatus() {
        return RoomStatus.BOOKED;
    }
}
