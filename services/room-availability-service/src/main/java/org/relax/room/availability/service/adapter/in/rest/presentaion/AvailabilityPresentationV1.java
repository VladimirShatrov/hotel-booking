package org.relax.room.availability.service.adapter.in.rest.presentaion;


public record AvailabilityPresentationV1(
        String roomId,
        Boolean availability
) {
}
