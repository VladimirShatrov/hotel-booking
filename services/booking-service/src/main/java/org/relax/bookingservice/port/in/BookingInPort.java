package org.relax.bookingservice.port.in;

import org.relax.bookingservice.domain.Booking;

public interface BookingInPort {
    Booking createBooking(Booking booking);
}
