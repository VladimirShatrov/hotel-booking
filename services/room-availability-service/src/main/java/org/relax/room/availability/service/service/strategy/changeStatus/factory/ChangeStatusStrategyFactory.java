package org.relax.room.availability.service.service.strategy.changeStatus.factory;

import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.changeStatus.RoomAvailabilityChangeStrategy;
import org.relax.room.availability.service.service.strategy.changeStatus.impl.BlockRoomStrategy;
import org.relax.room.availability.service.service.strategy.changeStatus.impl.BookRoomStrategy;
import org.relax.room.availability.service.service.strategy.changeStatus.impl.MaintenanceRoomStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ChangeStatusStrategyFactory {

    private final Map<RoomStatus, RoomAvailabilityChangeStrategy> strategies;

    public ChangeStatusStrategyFactory(final List<RoomAvailabilityChangeStrategy> strategies) {
        this.strategies = Map.of(
                RoomStatus.BLOCKED, getByType(strategies, BlockRoomStrategy.class),
                RoomStatus.MAINTENANCE, getByType(strategies, MaintenanceRoomStrategy.class),
                RoomStatus.BOOKED, getByType(strategies, BookRoomStrategy.class)
        );
    }

    public RoomAvailabilityChangeStrategy getStrategy(final RoomStatus status) {
        return Optional.ofNullable(strategies.get(status))
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + status));
    }

    private <T extends RoomAvailabilityChangeStrategy> T getByType(final List<RoomAvailabilityChangeStrategy> list,
                                                                     final Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing strategy: " + type.getSimpleName()));
    }

}
