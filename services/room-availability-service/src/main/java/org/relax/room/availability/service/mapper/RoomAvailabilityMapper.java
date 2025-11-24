package org.relax.room.availability.service.mapper;

import org.mapstruct.Mapper;
import org.relax.room.availability.service.adapter.in.rest.presentaion.RoomAvailabilityPresentationV1;
import org.relax.room.availability.service.domain.Room;
import org.relax.room.availability.service.domain.RoomAvailability;
import org.relax.room.availability.service.dto.RoomAvailabilityData;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomAvailabilityMapper {

    RoomAvailabilityData fromEntityToDto(RoomAvailability entity);
    RoomAvailability fromDtoToEntity(RoomAvailabilityData data);
    RoomAvailabilityPresentationV1 fromDtoToPresentationV1(RoomAvailabilityData data);

    List<RoomAvailability> fromDtoListToEntityList(List<RoomAvailabilityData> data);
    List<RoomAvailabilityData> fromEntityListToDtoList(List<RoomAvailability> data);
    List<RoomAvailabilityPresentationV1> fromDtoListToPresentationV1List(List<RoomAvailabilityData> data);


    default UUID map(Room room) {
        return room != null ? room.getId() : null;
    }

    default Room map(UUID id) {
        if (id == null) return null;
        Room room = new Room();
        room.setId(id);
        return room;
    }
}