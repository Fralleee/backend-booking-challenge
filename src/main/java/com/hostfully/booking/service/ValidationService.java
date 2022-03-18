package com.hostfully.booking.service;

import com.hostfully.booking.model.Block;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.repository.IBlockRepository;
import com.hostfully.booking.repository.IBookingRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

  private final IBookingRepository bookingRepository;

  private final IBlockRepository blockRepository;

  public Boolean sanityCheck(LocalDate startDate, LocalDate endDate) {
    if (startDate.compareTo(LocalDate.now()) < 0) {
      return false;
    }
    return endDate.compareTo(startDate) > 0;
  }

  public Boolean checkForOverlap(LocalDate startDate, LocalDate endDate, Long propertyId, Long id) {

    List<Block> blockOverlaps = blockRepository.findByDateOverlap(endDate, startDate, propertyId);
    if (!blockOverlaps.isEmpty()) {
      return false;
    }

    List<Booking> bookingOverlaps;
    if (id != null) {
      bookingOverlaps =
          bookingRepository.findByDateOverlapIgnoreSelf(endDate, startDate, propertyId, id);
    } else {
      bookingOverlaps = bookingRepository.findByDateOverlap(endDate, startDate, propertyId);
    }

    return bookingOverlaps.isEmpty();
  }
}
