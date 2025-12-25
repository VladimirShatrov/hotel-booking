package org.relax.roomcatalogservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.relax.roomcatalogservice.adapter.in.rest.presentation.HotelPresentation;
import org.relax.roomcatalogservice.dto.CreateHotelRequest;
import org.relax.roomcatalogservice.dto.UpdateHotelRequest;
import org.relax.roomcatalogservice.port.in.HotelInPort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hotels")
@Validated
public class HotelController {

    private final HotelInPort service;

    @PostMapping
    public ResponseEntity<HotelPresentation> createHotel(@RequestBody @Validated CreateHotelRequest request) {
        return ResponseEntity.ok(service.createHotel(request));
    }

    @GetMapping
    public ResponseEntity<List<HotelPresentation>> getAllHotels() {
        return ResponseEntity.ok(service.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelPresentation> getHotelById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getHotelById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelPresentation> updateHotel(@PathVariable UUID id,
                                                         @RequestBody @Validated UpdateHotelRequest request) {
        return ResponseEntity.ok(service.updateHotel(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        service.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
