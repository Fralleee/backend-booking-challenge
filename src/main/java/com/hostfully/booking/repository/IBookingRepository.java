package com.hostfully.booking.repository;

import com.hostfully.booking.model.Booking;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookingRepository extends JpaRepository<Booking, Long> {

  @Query(
      "SELECT b FROM Booking b WHERE b.endDate >= ?2 AND b.startDate <= ?1 AND b.propertyId = ?3")
  List<Booking> findByDateOverlap(LocalDate startDate, LocalDate endDate, Long propertyId);

  @Query(
      "SELECT b FROM Booking b WHERE b.endDate >= ?2 AND b.startDate <= ?1 AND b.propertyId = ?3 AND b.id <> ?4")
  List<Booking> findByDateOverlapIgnoreSelf(
      LocalDate startDate, LocalDate endDate, Long propertyId, Long id);
}
