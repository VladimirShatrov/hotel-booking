package org.relax.room.availability.service.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.port.in.RoomInPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaRoomAdapter {

    private final RoomInPort inPort;

    @KafkaListener(
            topics = "create-room", groupId = "room-available-service-group"
    )
    public void receiveNewRoom(Room room) {
        inPort.save(room);
    }

    @KafkaListener(
            topics = "delete-room", groupId = "room-available-service-group"
    )
    public void receiveRoomDeletingCommand(Room room) {
        inPort.delete(room);
    }
}
