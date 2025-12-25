package org.relax.bookingservice.dto;

import org.relax.bookingservice.domain.BookingStatus;

import java.util.UUID;

public record BookingData(
        UUID id,
        UUID userId,
        UUID intervalId,
        BookingStatus status
) {
}
