package org.relax.room.availability.service.service.strategy.overlappingPolicy.impl;

import jakarta.persistence.PersistenceException;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.util.OverlapUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Component
public class MergeOverlappingStrategy implements OverlappingStrategy {

    private final List<RoomAvailability> allExisting;

    public MergeOverlappingStrategy(List<RoomAvailability> allExisting) {
        this.allExisting = allExisting;
    }

    @Transactional
    @Override

    //FIXME объединенные интервалы не удаляются
    public RoomAvailability resolve(Room room, List<RoomAvailability> overlaps,
                                          RoomStatus newStatus, LocalDateTime startDate,
                                          LocalDateTime endDate) {
        List<RoomAvailability> sameTypeOverlaps = overlaps.stream()
                .filter(a -> a.getStatus() == newStatus)
                .toList();

        if (sameTypeOverlaps.isEmpty()) {
            return RoomAvailability.builder()
                            .id(UUID.randomUUID())
                            .roomId(room)
                            .status(newStatus)
                            .startDate(startDate)
                            .endDate(endDate)
                            .build();
        }

        LocalDateTime mergedStart = sameTypeOverlaps.stream()
                .map(RoomAvailability::getStartDate)
                .min(Comparator.naturalOrder())
                .orElse(startDate);

        LocalDateTime mergedEnd = sameTypeOverlaps.stream()
                .map(RoomAvailability::getEndDate)
                .max(Comparator.naturalOrder())
                .orElse(endDate);

        if (startDate.isBefore(mergedStart)) mergedStart = startDate;
        if (endDate.isAfter(mergedEnd)) mergedEnd = endDate;

        final LocalDateTime finalMergedStart = mergedStart;
        final LocalDateTime finalMergedEnd = mergedEnd;
        boolean intersectsWithOtherStatuses = allExisting.stream()
                .filter(a -> a.getStatus() != newStatus)
                .anyMatch(a -> OverlapUtils.isOverlapping(
                        a.getStartDate(), a.getEndDate(),
                        finalMergedStart, finalMergedEnd
                ));

        if (intersectsWithOtherStatuses) {
            throw new PersistenceException(String.format(
                    "Cannot merge intervals for %s: merged interval [%s - %s] overlaps other statuses",
                    room.getId(), mergedStart, mergedEnd
            ));
        }

        RoomAvailability merged = RoomAvailability.builder()
                .id(UUID.randomUUID())
                .roomId(room)
                .status(newStatus)
                .startDate(finalMergedStart)
                .endDate(finalMergedEnd)
                .build();

        return merged;
    }
}
