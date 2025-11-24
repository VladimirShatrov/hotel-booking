package org.relax.room.availability.service.port.in;

import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.dto.RoomAvailabilityData;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomAvailabilityInPort {

    Optional<RoomAvailabilityData> findRoomAvailabilityInTimeInterval(UUID roomId, LocalDateTime startDate, LocalDateTime endDate);

    RoomAvailabilityData findRoomAvailabilityById(UUID id);

    RoomAvailabilityData findLatestUnavailablePeriodByRoomId(UUID roomId);

    void delete(UUID id);

    RoomAvailabilityData save(RoomAvailabilityData availability);

    RoomAvailabilityData changeStatus(UUID roomId, RoomStatus newStatus, LocalDateTime startDate,
                                      LocalDateTime endDate, OverlappingPolicy overlappingPolicy, String reason);

    List<RoomAvailabilityData> findAllUnavailablePeriodByRoomId(UUID roomId);
}