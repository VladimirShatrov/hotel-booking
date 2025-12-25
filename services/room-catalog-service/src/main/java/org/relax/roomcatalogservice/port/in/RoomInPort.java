package org.relax.roomcatalogservice.port.in;

import org.relax.roomcatalogservice.adapter.in.rest.presentation.RoomPresentation;
import org.relax.roomcatalogservice.dto.CreateRoomRequest;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;

import java.util.List;
import java.util.UUID;

public interface RoomInPort {
    RoomPresentation createRoom(CreateRoomRequest request);
    RoomPresentation getRoomById(UUID id);
    List<RoomPresentation> getAllRooms();
    RoomPresentation updateRoom(UUID id, UpdateRoomRequest request);
    void deleteRoom(UUID id);
    boolean existsById(UUID id);
}
