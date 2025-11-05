package org.relax.room.availability.service.adapter.in.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.relax.room.availability.service.adapter.in.rest.command.ChangeRoomAvailabilityRequest;
import org.relax.room.availability.service.adapter.in.rest.presentaion.AvailabilityPresentationV1;
import org.relax.room.availability.service.adapter.in.rest.presentaion.RoomAvailabilityPresentationV1;
import org.relax.room.availability.service.dto.RoomAvailabilityData;
import org.relax.room.availability.service.mapper.RoomAvailabilityMapper;
import org.relax.room.availability.service.port.in.RoomAvailabilityInPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/availability")
public class RoomAvailabilityController {

    private static final String V1_ROOM_AVAILABILITY_MEDIA_TYPE = "application/vnd.org.relax.room-availability.full.presentation.v1+json";
    private static final String V1_ROOM_AVAILABILITY_STATUS_MEDIA_TYPE = "application/vnd.org.relax.room-availability.status.presentation.v1+json";

    private final RoomAvailabilityInPort inPort;
    private final RoomAvailabilityMapper roomAvailabilityMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RoomAvailabilityData> getById(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(V1_ROOM_AVAILABILITY_MEDIA_TYPE))
                .body(inPort.findRoomAvailabilityById(UUID.fromString(id)));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<AvailabilityPresentationV1> isRoomAvailableInTimeInterval(
            @PathVariable UUID roomId,
            @RequestParam LocalDateTime startTimeInterval,
            @RequestParam LocalDateTime endTimeInterval
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

    @PostMapping("/change")
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        inPort.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
