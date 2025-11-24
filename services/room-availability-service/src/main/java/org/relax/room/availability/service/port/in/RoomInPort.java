package org.relax.room.availability.service.port.in;

import org.relax.room.availability.service.domain.Room;

import java.util.UUID;

public interface RoomInPort {

    void save(Room room);

    void delete(Room room);

    boolean exists(UUID id);
}
