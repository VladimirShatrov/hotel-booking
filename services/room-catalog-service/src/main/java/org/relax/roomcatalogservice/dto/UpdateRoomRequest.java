package org.relax.roomcatalogservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateRoomRequest(
        @JsonProperty("name")
        String name,
        @JsonProperty("capacity")
        Integer capacity,
        @JsonProperty("location")
        String location
) {}
