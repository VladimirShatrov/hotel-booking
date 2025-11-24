package org.relax.room.availability.service.adapter.in.rest.command;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.relax.room.availability.service.domain.RoomStatus;
import org.relax.room.availability.service.service.strategy.overlappingPolicy.OverlappingPolicy;

import java.time.LocalDateTime;

public record ChangeRoomAvailabilityRequest(
        @NotBlank
        String roomId,
        @NotNull
        LocalDateTime start,
        @NotNull
        LocalDateTime end,
        @NotNull
        RoomStatus status,
        @NotNull
        OverlappingPolicy overlappingPolicy,
        String reason
) {
    @AssertTrue(message = "Status 'BOOKED' cannot be set manually")
    public boolean isValidStatus() {
        return status != RoomStatus.BOOKED;
    }

    @AssertTrue(message = "End must be after start")
    public boolean isValidTimeInterval() {
        return end != null && end.isAfter(start);
    }
}
