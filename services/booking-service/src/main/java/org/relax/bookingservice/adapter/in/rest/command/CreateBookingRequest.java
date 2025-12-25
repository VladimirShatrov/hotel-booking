package org.relax.bookingservice.adapter.in.rest.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateBookingRequest(
        UUID userId,
        UUID roomId,
        LocalDateTime start,
        LocalDateTime end
) {
}
