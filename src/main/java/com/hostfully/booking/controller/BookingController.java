package com.hostfully.booking.controller;

import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.request.CreateBlockRequest;
import com.hostfully.booking.model.request.CreateBookingRequest;
import com.hostfully.booking.model.request.UpdateBookingRequest;
import com.hostfully.booking.service.BlockService;
import com.hostfully.booking.service.BookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;

  private final BlockService blockService;

  @GetMapping
  public List<Booking> findAll() {
    return bookingService.findAll();
  }

  @GetMapping(value = "/{id}")
  public Booking findById(@PathVariable("id") Long id) {
    return bookingService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody CreateBookingRequest request) {
    return bookingService.create(request);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable("id") Long id, @RequestBody UpdateBookingRequest request) {
    bookingService.update(id, request);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") Long id) {
    bookingService.delete(id);
  }

  @PostMapping(value = "/block")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createBlock(@RequestBody CreateBlockRequest request) {
    return blockService.create(request);
  }

  @DeleteMapping(value = "/block/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteBlock(@PathVariable("id") Long id) {
    blockService.delete(id);
  }
}
