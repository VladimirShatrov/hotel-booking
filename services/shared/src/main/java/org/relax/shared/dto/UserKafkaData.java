package org.relax.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserKafkaData(
        @NotNull
        UUID id,

        @NotBlank
        String email
) {
}

