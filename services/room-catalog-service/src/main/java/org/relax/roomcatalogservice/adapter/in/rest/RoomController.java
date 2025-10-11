package org.relax.roomcatalogservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.RoomPresentation;
import org.relax.roomcatalogservice.dto.CreateRoomRequest;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.port.in.RoomInPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rooms")
@Validated
public class RoomController {

    private static final String MEDIA_TYPE = "application/vnd.org.relax.room.v1+json";
    private final RoomInPort service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MEDIA_TYPE)
    public ResponseEntity<RoomPresentation> createRoom(@RequestBody @Validated CreateRoomRequest request) {
        return ResponseEntity.ok(service.createRoom(request));
    }

    @GetMapping(produces = MEDIA_TYPE)
    public ResponseEntity<List<RoomPresentation>> getAllRooms() {
        return ResponseEntity.ok(service.getAllRooms());
    }

    @GetMapping(path = "/{roomId}", produces = MEDIA_TYPE)
    public ResponseEntity<RoomPresentation> getRoom(@PathVariable UUID roomId) {
        return ResponseEntity.ok(service.getRoomById(roomId));
    }

    @PutMapping(path = "/{roomId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MEDIA_TYPE)
    public ResponseEntity<RoomPresentation> updateRoom(@PathVariable UUID roomId,
                                                       @RequestBody @Validated UpdateRoomRequest request) {
        return ResponseEntity.ok(service.updateRoom(roomId, request));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID roomId) {
        service.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
