package org.relax.room.availability.service.service;

import lombok.RequiredArgsConstructor;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.port.in.RoomInPort;
import org.relax.room.availability.service.port.out.RoomOutPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService implements RoomInPort {

    private final RoomOutPort outPort;

    @Override
    public void save(Room room) {
        outPort.save(room);
    }

    @Override
    public void delete(Room room) {
        outPort.delete(room);
    }

    @Override
    public boolean exists(UUID id) {
        return outPort.exists(id);
    }
}
