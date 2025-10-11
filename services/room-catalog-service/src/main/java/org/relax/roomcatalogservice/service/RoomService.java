package org.relax.roomcatalogservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.dto.RoomData;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.mapper.RoomMapper;
import org.relax.roomcatalogservice.port.in.RoomInPort;
import org.relax.roomcatalogservice.repository.RoomRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService implements RoomInPort {

    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Override
    public RoomData getRoomById(UUID id) {
        return repository.findById(id)
                .map(mapper::entityToDto)
                .orElseThrow(() -> new EntityNotFoundException("Комната с id: " + id + " не найдена"));
    }

    @Override
    @Transactional
    public RoomData updateRoom(UUID id, UpdateRoomRequest data) {
        Room room = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комната с id: " + id + " не найдена"));

        if (data.name() != null) room.setName(data.name());
        if (data.capacity() != null) room.setCapacity(data.capacity());
        if (data.location() != null) room.setLocation(data.location());

        return mapper.entityToDto(repository.save(room));
    }

    @Override
    @Transactional
    public RoomData save(RoomData data) {
        Room room = mapper.dtoToEntity(data);
        repository.save(room);
        return data;
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
