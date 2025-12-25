package org.relax.roomcatalogservice.service;

import lombok.RequiredArgsConstructor;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.RoomPresentation;
import org.relax.roomcatalogservice.domain.Hotel;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.dto.CreateRoomRequest;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;
import org.relax.roomcatalogservice.mapper.RoomMapper;
import org.relax.roomcatalogservice.port.in.RoomInPort;
import org.relax.roomcatalogservice.port.out.RoomRepository;
import org.relax.roomcatalogservice.port.out.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService implements RoomInPort {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper mapper;

    @Override
    public RoomPresentation createRoom(CreateRoomRequest req) {
        Hotel hotel = hotelRepository.findById(UUID.fromString(req.hotelId()))
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));

        Room room = Room.builder()
                .id(UUID.randomUUID())
                .number(req.number())
                .capacity(req.capacity())
                .hotel(hotel)
                .amenities(req.amenities())
                .build();

        roomRepository.save(room);
        return mapper.entityToPresentation(room);
    }

    @Override
    public RoomPresentation getRoomById(UUID id) {
        var room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        return mapper.entityToPresentation(room);
    }

    @Override
    public List<RoomPresentation> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(mapper::entityToPresentation)
                .toList();
    }

    @Override
    public RoomPresentation updateRoom(UUID id, UpdateRoomRequest req) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (req.number() != null) room.setNumber(req.number());
        if (req.capacity() != null) room.setCapacity(req.capacity());
        if (req.amenities() != null) room.setAmenities(req.amenities());

        if (req.hotelId() != null) {
            Hotel hotel = hotelRepository.findById(UUID.fromString(req.hotelId()))
                    .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
            room.setHotel(hotel);
        }

        roomRepository.save(room);
        return mapper.entityToPresentation(room);
    }

    @Override
    public void deleteRoom(UUID id) {
        if (!roomRepository.existsById(id))
            throw new IllegalArgumentException("Room not found");
        roomRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return roomRepository.existsById(id);
    }
}
