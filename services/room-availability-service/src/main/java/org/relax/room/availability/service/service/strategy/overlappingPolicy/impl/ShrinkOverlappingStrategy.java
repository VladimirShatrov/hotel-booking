package org.relax.room.availability.service.service.strategy.overlappingPolicy.impl;

import jakarta.transaction.Transactional;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.util.OverlapUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ShrinkOverlappingStrategy implements OverlappingStrategy {

    @Transactional
    @Override
    public RoomAvailability resolve(Room room, List<RoomAvailability> overlaps,
                                    RoomStatus newStatus, LocalDateTime startDate,
                                    LocalDateTime endDate) {

        LocalDateTime adjustedStart = startDate;
        LocalDateTime adjustedEnd = endDate;

        for (RoomAvailability existing : overlaps) {
            LocalDateTime s = existing.getStartDate();
            LocalDateTime e = existing.getEndDate();
//FIXME добавить кастомные исключения
            if (OverlapUtils.isFullyContained(adjustedStart, adjustedEnd, s, e)) {
                throw new RuntimeException("Новый интервал полностью внутри существующего, shrink невозможен");
            }

            if (OverlapUtils.overlapsFromLeft(adjustedStart, adjustedEnd, s, e)) {
                adjustedEnd = s.minusSeconds(1);
            }

            if (OverlapUtils.overlapsFromRight(adjustedStart, adjustedEnd, s, e)) {
                adjustedStart = e.plusSeconds(1);
            }

            if (!OverlapUtils.isValid(adjustedStart, adjustedEnd)) {
                throw new RuntimeException("После shrink новый интервал стал некорректным");
            }
        }

        return RoomAvailability.builder()
                .id(UUID.randomUUID())
                .roomId(room)
                .startDate(adjustedStart)
                .endDate(adjustedEnd)
                .status(newStatus)
                .build();
    }
}

