package org.relax.roomcatalogservice.mapper;

import org.mapstruct.Mapper;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.dto.RoomData;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {
    public abstract RoomData entityToDto(Room room);
    public abstract Room dtoToEntity(RoomData data);
}
