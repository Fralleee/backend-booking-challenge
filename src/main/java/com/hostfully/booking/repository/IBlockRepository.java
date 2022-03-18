package com.hostfully.booking.repository;

import com.hostfully.booking.model.Block;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlockRepository extends JpaRepository<Block, Long> {

  @Query("SELECT b FROM Block b WHERE b.endDate >= ?2 AND b.startDate <= ?1 AND b.propertyId = ?3")
  List<Block> findByDateOverlap(LocalDate startDate, LocalDate endDate, Long propertyId);
}
