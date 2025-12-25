package org.relax.bookingservice.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.relax.bookingservice.adapter.in.rest.command.CreateBookingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final BookingInPort inPort;

    @PostMapping()
    public ResponseEntity<?> createBooking(@RequestBody final CreateBookingRequest request) {
        var booking = inPort.createBooking(
                request.roomId(),
                request.userId(),
                request.start(),
                request.end()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(booking);
    }
}
