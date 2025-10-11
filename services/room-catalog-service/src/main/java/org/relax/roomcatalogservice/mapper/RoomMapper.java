package org.relax.roomcatalogservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.RoomPresentation;
import org.relax.roomcatalogservice.domain.Room;
import org.relax.roomcatalogservice.domain.Hotel;
import org.relax.roomcatalogservice.dto.UpdateRoomRequest;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "hotel", expression = "java(hotelToPresentation(room.getHotel()))")
    RoomPresentation entityToPresentation(Room room);

    default Room updateRequestToEntity(UpdateRoomRequest req, Hotel hotel) {
        if (req == null) return null;
        return Room.builder()
                .number(req.number())
                .capacity(req.capacity())
                .hotel(hotel)
                .amenities(req.amenities())
                .build();
    }

    default org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation hotelToPresentation(Hotel hotel) {
        if (hotel == null) return null;
        var loc = hotel.getLocation();
        return new org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation(
                hotel.getId(),
                hotel.getName(),
                loc != null ? loc.latitude() : null,
                loc != null ? loc.longitude() : null
        );
    }
}
