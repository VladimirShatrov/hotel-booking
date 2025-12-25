package org.relax.roomcatalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;

public record CreateRoomRequest(
        @NotBlank String number,
        @NotNull @Positive Integer capacity,
        @NotNull String hotelId,
        Set<String> amenities
) {}
