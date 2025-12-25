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
    }

    public static boolean overlapsFromLeft(LocalDateTime newStart, LocalDateTime newEnd,
                                           LocalDateTime existingStart, LocalDateTime existingEnd) {
        return newStart.isBefore(existingStart) && newEnd.isAfter(existingStart) && newEnd.isBefore(existingEnd);
    }

    public static boolean overlapsFromRight(LocalDateTime newStart, LocalDateTime newEnd,
                                            LocalDateTime existingStart, LocalDateTime existingEnd) {
        return newStart.isAfter(existingStart) && newStart.isBefore(existingEnd) && newEnd.isAfter(existingEnd);
    }

    public static boolean fullyCovers(LocalDateTime outerStart, LocalDateTime outerEnd,
                                      LocalDateTime innerStart, LocalDateTime innerEnd) {
        return !outerStart.isAfter(innerStart) && !outerEnd.isBefore(innerEnd);
    }

    public static boolean isValid(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    public static boolean isTouching(LocalDateTime end1, LocalDateTime start2) {
        return end1.equals(start2);
    }
}
