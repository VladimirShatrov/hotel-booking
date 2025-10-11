package org.relax.roomcatalogservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.relax.roomcatalogservice.dto.RoomData;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.port.in.RoomInPort;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/room")
public class RoomController {

    private static final String MEDIA_TYPE = "application/vnd.t1internship.room.v1+json";
    private final RoomInPort service;

    @GetMapping(path = "/{roomId}", produces = MEDIA_TYPE)
    public ResponseEntity<RoomData> getRoom(@PathVariable UUID roomId) {
        RoomData data = service.getRoomById(roomId);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MEDIA_TYPE)).body(data);
    }

    @PutMapping(path = "/{roomId}", produces = MEDIA_TYPE)
    public ResponseEntity<RoomData> updateRoom(@PathVariable UUID roomId,
                                               @RequestBody UpdateRoomRequest request) {
        RoomData updated = service.updateRoom(roomId, request);
        return ResponseEntity.ok().contentType(MediaType.valueOf(MEDIA_TYPE)).body(updated);
    }

    @GetMapping("/{roomId}/exists")
    public ResponseEntity<Void> exists(@PathVariable UUID roomId) {
        return service.existsById(roomId) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
