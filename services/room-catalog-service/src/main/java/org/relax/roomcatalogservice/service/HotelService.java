package org.relax.roomcatalogservice.service;

import lombok.RequiredArgsConstructor;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation;
import org.relax.roomcatalogservice.domain.geo.HotelLocation;
import org.relax.roomcatalogservice.domain.Hotel;
import org.relax.roomcatalogservice.dto.CreateHotelRequest;
import org.relax.roomcatalogservice.dto.UpdateHotelRequest;
import org.relax.roomcatalogservice.port.in.HotelInPort;
import org.relax.roomcatalogservice.port.out.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService implements HotelInPort {

    private final HotelRepository repository;

    @Override
    public HotelPresentation createHotel(CreateHotelRequest req) {
        Hotel hotel = Hotel.builder()
                .id(UUID.randomUUID())
                .name(req.name())
                .location(new HotelLocation(req.latitude(), req.longitude()))
                .build();
        repository.save(hotel);
        return HotelPresentation.from(hotel);
    }

    @Override
    public HotelPresentation getHotelById(UUID id) {
        Hotel hotel = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
        return HotelPresentation.from(hotel);
    }

    @Override
    public List<HotelPresentation> getAllHotels() {
        return repository.findAll().stream()
                .map(HotelPresentation::from)
                .toList();
    }

    @Override
    public HotelPresentation updateHotel(UUID id, UpdateHotelRequest req) {
        Hotel hotel = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
        hotel.setName(req.name());
        if (req.latitude() != null && req.longitude() != null)
            hotel.setLocation(new HotelLocation(req.latitude(), req.longitude()));
        repository.save(hotel);
        return HotelPresentation.from(hotel);
    }

    @Override
    public void deleteHotel(UUID id) {
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Hotel not found");
        repository.deleteById(id);
    }
}
