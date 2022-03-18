package com.hostfully.booking.service;

import com.hostfully.booking.exception.InvalidDateException;
import com.hostfully.booking.exception.NotFoundException;
import com.hostfully.booking.mapper.BookingMapper;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.request.CreateBookingRequest;
import com.hostfully.booking.model.request.UpdateBookingRequest;
import com.hostfully.booking.repository.IBookingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final IBookingRepository bookingRepository;

  private final ValidationService validationService;

  public List<Booking> findAll() {
    return bookingRepository.findAll();
  }

  public Booking findById(Long id) {
    return bookingRepository.findById(id).orElseThrow(NotFoundException::new);
  }

  public Long create(CreateBookingRequest request) {
    Boolean validDates =
        validationService.sanityCheck(request.getStartDate(), request.getEndDate());
    if (Boolean.FALSE.equals(validDates)) {
      throw new InvalidDateException();
    }

    Boolean hasOverlap =
        validationService.checkForOverlap(
            request.getStartDate(), request.getEndDate(), request.getPropertyId(), null);
    if (Boolean.FALSE.equals(hasOverlap)) {
      throw new InvalidDateException();
    }

    Booking booking =
        new Booking(
            null,
            request.getPropertyId(),
            request.getTenantId(),
            request.getStartDate(),
            request.getEndDate());
    return bookingRepository.save(booking).getId();
  }

  public void update(Long id, UpdateBookingRequest request) {
    if (Boolean.FALSE.equals(
        validationService.sanityCheck(request.getStartDate(), request.getEndDate()))) {
      throw new InvalidDateException();
    }

    Booking booking = bookingRepository.findById(id).orElseThrow(NotFoundException::new);

    Boolean hasOverlap =
        validationService.checkForOverlap(
            request.getStartDate(), request.getEndDate(), booking.getPropertyId(), id);
    if (Boolean.FALSE.equals(hasOverlap)) {
      throw new InvalidDateException();
    }

    BookingMapper.updateBooking(booking, request);
    bookingRepository.save(booking);
  }

  public void delete(Long id) {
    bookingRepository.findById(id).orElseThrow(NotFoundException::new);
    bookingRepository.deleteById(id);
  }
}
