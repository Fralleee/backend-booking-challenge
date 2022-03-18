package com.hostfully.booking.mapper;

import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.request.UpdateBookingRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

  public static void updateBooking(Booking booking, UpdateBookingRequest request) {
    if (request.getStartDate() != null) {
      booking.setStartDate(request.getStartDate());
    }

    if (request.getEndDate() != null) {
      booking.setEndDate(request.getEndDate());
    }
  }
}
