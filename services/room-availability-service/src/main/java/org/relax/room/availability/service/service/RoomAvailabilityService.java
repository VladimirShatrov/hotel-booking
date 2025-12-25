package org.relax.room.availability.service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.dto.RoomAvailabilityData;
import org.relax.room.availability.service.mapper.RoomAvailabilityMapper;
import org.relax.room.availability.service.port.in.RoomAvailabilityInPort;
import org.relax.room.availability.service.port.out.RoomAvailabilityOutPort;
import org.relax.room.availability.service.port.out.RoomOutPort;
import org.relax.room.availability.service.service.strategy.changeStatus.factory.ChangeStatusStrategyFactory;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomAvailabilityService implements RoomAvailabilityInPort {

    private final RoomAvailabilityOutPort availabilityOutPort;
    private final RoomOutPort roomOutPort;
    private final RoomAvailabilityMapper mapper;
    private final ChangeStatusStrategyFactory changeStatusStrategyFactory;


    @Override
    public Optional<RoomAvailabilityData> findRoomAvailabilityInTimeInterval(UUID roomId, LocalDateTime startDate, LocalDateTime endDate) {
        Room room = roomOutPort.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with id " + roomId + " not found")
        );
        
        return availabilityOutPort.findByRoomIdAndStartDateAfterAndEndDateBefore(room, startDate, endDate)
                .map(mapper::fromEntityToDto);
    }

    @Override
    public RoomAvailabilityData findRoomAvailabilityById(UUID id) {
        return mapper.fromEntityToDto(availabilityOutPort.findRoomAvailableById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Room availability with id " + id + " not found")
                )
        );
    }

    @Override
    public RoomAvailabilityData findLatestUnavailablePeriodByRoomId(UUID roomId) {
        Room room = roomOutPort.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with id " + roomId + " not found")
        );

        return mapper.fromEntityToDto(availabilityOutPort.findLatestUnavailablePeriodByRoomId(room)
                .orElseThrow(
                        () -> new EntityNotFoundException("Room availability by room " + roomId + " not found")
                )
        );
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        availabilityOutPort.deleteById(id);
    }

    @Override
    @Transactional
    public RoomAvailabilityData save(RoomAvailabilityData availability) {
        if (availabilityOutPort.existsAnyFromStartDateAndToEndDate(availability.startDate(), availability.endDate())) {
            throw new RuntimeException("Room availability already exists in this interval");
        }
        return mapper.fromEntityToDto(
                availabilityOutPort.save(
                        mapper.fromDtoToEntity(availability)
                )
        );
    }

    @Override
    @Transactional
    public RoomAvailabilityData changeStatus(UUID roomId, RoomStatus newStatus, LocalDateTime startDate,
                                             LocalDateTime endDate, OverlappingPolicy overlappingPolicy, String reason) {
        Room room = roomOutPort.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with id " + roomId + " not found")
        );

        var overlapping = availabilityOutPort.findNestedAvailabilityIntervalsByRoom(room, startDate, endDate);
        if (!overlapping.isEmpty()) {
            log.warn("Trying to rewrite existing room availability interval for room {}", roomId);
        }

        return mapper.fromEntityToDto(changeStatusStrategyFactory.getStrategy(newStatus).apply(
                room,
                startDate,
                endDate,
                overlappingPolicy,
                reason
        ));
    }

    @Override
    public List<RoomAvailabilityData> findAllUnavailablePeriodByRoomId(UUID roomId) {
        Room room = roomOutPort.findById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with id " + roomId + " not found")
        );

        return availabilityOutPort.findByRoom(room).stream()
                .map(mapper::fromEntityToDto)
                .toList();
    }
}
