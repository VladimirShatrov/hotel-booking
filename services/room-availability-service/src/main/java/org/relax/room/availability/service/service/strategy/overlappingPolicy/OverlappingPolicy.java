package org.relax.room.availability.service.service.strategy.overlappingPolicy;

public enum OverlappingPolicy {
    REJECT, // отклонить если есть пересечение
    MERGE, // объеденить с существующим, того же типа
    SHRINK // сократить новый интервал, что бы он не пересекалься
}
