package org.relax.roomcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHotelRequest(
        @NotBlank String name,
        @NotNull Double latitude,
        @NotNull Double longitude
) {}
