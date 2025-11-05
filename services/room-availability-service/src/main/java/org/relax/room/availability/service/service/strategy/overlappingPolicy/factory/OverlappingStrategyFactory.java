package org.relax.room.availability.service.service.strategy.overlappingPolicy.factory;

import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.impl.MergeOverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.impl.RejectOverlappingStrategy;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.impl.ShrinkOverlappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OverlappingStrategyFactory {

    private final Map<OverlappingPolicy, OverlappingStrategy> strategies;

    public OverlappingStrategyFactory(List<OverlappingStrategy> strategies) {
        this.strategies = Map.of(
                OverlappingPolicy.MERGE, getByType(strategies, MergeOverlappingStrategy.class),
                OverlappingPolicy.REJECT, getByType(strategies, RejectOverlappingStrategy.class),
                OverlappingPolicy.SHRINK, getByType(strategies, ShrinkOverlappingStrategy.class)
        );
    }

    public OverlappingStrategy getStrategy(OverlappingPolicy policy) {
        return strategies.getOrDefault(policy, strategies.get(OverlappingPolicy.REJECT));
    }

    private <T extends OverlappingStrategy> T getByType(final List<OverlappingStrategy> list, Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing strategy: " + type.getSimpleName()));
    }

}
