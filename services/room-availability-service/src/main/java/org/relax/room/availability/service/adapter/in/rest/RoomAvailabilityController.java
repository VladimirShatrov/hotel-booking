package org.relax.room.availability.service.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.relax.room.availability.service.adapter.in.rest.command.ChangeRoomAvailabilityRequest;
import org.relax.room.availability.service.adapter.in.rest.presentaion.AvailabilityPresentationV1;
import org.relax.room.availability.service.adapter.in.rest.presentaion.RoomAvailabilityPresentationV1;
import org.relax.room.availability.service.dto.RoomAvailabilityData;
import org.relax.room.availability.service.mapper.RoomAvailabilityMapper;
import org.relax.room.availability.service.port.in.RoomAvailabilityInPort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/availability")
public class RoomAvailabilityController {

    private static final String V1_ROOM_AVAILABILITY_MEDIA_TYPE = "application/vnd.org.relax.room-availability.full.presentation.v1+json";
    private static final String V1_ROOM_AVAILABILITY_STATUS_MEDIA_TYPE = "application/vnd.org.relax.room-availability.status.presentation.v1+json";

    private final RoomAvailabilityInPort inPort;
    private final RoomAvailabilityMapper roomAvailabilityMapper;

    @GetMapping(
            value = "/{id}",
            produces = V1_ROOM_AVAILABILITY_MEDIA_TYPE
    )
    public ResponseEntity<RoomAvailabilityData> getById(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(V1_ROOM_AVAILABILITY_MEDIA_TYPE))
                .body(inPort.findRoomAvailabilityById(UUID.fromString(id)));
    }

    @GetMapping(
            value = "/room/{roomId}",
            produces = V1_ROOM_AVAILABILITY_STATUS_MEDIA_TYPE
    )
    public ResponseEntity<AvailabilityPresentationV1> isRoomAvailableInTimeInterval(
            @PathVariable UUID roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTimeInterval,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTimeInterval
            ) {
        var data = inPort.findRoomAvailabilityInTimeInterval(roomId, startTimeInterval, endTimeInterval);

        AvailabilityPresentationV1 result;

        if (data.isEmpty()) {
            result = new AvailabilityPresentationV1(roomId.toString(), true);
        } else {
            result = new AvailabilityPresentationV1(roomId.toString(), false);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(V1_ROOM_AVAILABILITY_STATUS_MEDIA_TYPE))
                .body(result);
    }

    @PostMapping(
            value = "/change",
            produces = V1_ROOM_AVAILABILITY_MEDIA_TYPE
    )
    public ResponseEntity<RoomAvailabilityPresentationV1> changeRoomAvailabilityStatus(
            @Valid @RequestBody ChangeRoomAvailabilityRequest request
            ) {

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(V1_ROOM_AVAILABILITY_MEDIA_TYPE))
                .body(roomAvailabilityMapper.fromDtoToPresentationV1(inPort.changeStatus(
                        UUID.fromString(request.roomId()),
                        request.status(),
                        request.start(),
                        request.end(),
                        request.overlappingPolicy(),
                        request.reason()
                )));

    }

    @GetMapping(
            value = "/all/by/room/{roomId}",
            produces = V1_ROOM_AVAILABILITY_MEDIA_TYPE
    )
    public ResponseEntity<List<RoomAvailabilityPresentationV1>> getAllByRoom(@PathVariable UUID roomId) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(V1_ROOM_AVAILABILITY_MEDIA_TYPE))
                .body(inPort.findAllUnavailablePeriodByRoomId(roomId).stream()
                        .map(roomAvailabilityMapper::fromDtoToPresentationV1)
                        .toList()
                );
    }

    @DeleteMapping(
            value = "/{id}"
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        inPort.delete(id);
        return ResponseEntity.noContent().build();
    }

}
