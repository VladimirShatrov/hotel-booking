package org.relax.roomcatalogservice.port.in;

import org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation;
import org.relax.roomcatalogservice.dto.CreateHotelRequest;
import org.relax.roomcatalogservice.dto.UpdateHotelRequest;

import java.util.List;
import java.util.UUID;

public interface HotelInPort {
    HotelPresentation createHotel(CreateHotelRequest request);
    HotelPresentation getHotelById(UUID id);
    List<HotelPresentation> getAllHotels();
    HotelPresentation updateHotel(UUID id, UpdateHotelRequest request);
    void deleteHotel(UUID id);
}
