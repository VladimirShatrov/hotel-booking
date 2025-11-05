package org.relax.room.availability.service.service.strategy.overlappingPolicy.util;

import java.time.LocalDateTime;

public final class OverlapUtils {

    private OverlapUtils() {}

    public static boolean isOverlapping(final LocalDateTime start1, final LocalDateTime end1,
                                        final LocalDateTime start2, final LocalDateTime end2) {
        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }

    public static boolean isFullyContained(LocalDateTime innerStart, LocalDateTime innerEnd,
                                           LocalDateTime outerStart, LocalDateTime outerEnd) {
        return !innerStart.isBefore(outerStart) && !innerEnd.isAfter(outerEnd);
    }}
