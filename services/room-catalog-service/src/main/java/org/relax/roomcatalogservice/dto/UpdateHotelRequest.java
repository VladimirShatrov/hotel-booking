package org.relax.roomcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateHotelRequest(
        @NotBlank String name,
        Double latitude,
        Double longitude
) {}
