package org.relax.roomcatalogservice.port.in;

import org.relax.roomcatalogservice.dto.RoomData;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;

import java.util.UUID;

public interface RoomInPort {
    RoomData getRoomById(UUID id);
    RoomData updateRoom(UUID id, UpdateRoomRequest data);
    RoomData save(RoomData data);
    boolean existsById(UUID id);
}
