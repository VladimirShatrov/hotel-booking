package org.relax.roomcatalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record UpdateRoomRequest(
        @JsonProperty("number")
        @Size(min = 1, max = 50)
        String number,

        @JsonProperty("capacity")
        @Positive
        Integer capacity,

        @JsonProperty("hotelId")
        String hotelId,

        @JsonProperty("amenities")
        Set<String> amenities
) {}
