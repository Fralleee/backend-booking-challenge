package com.hostfully.booking.service;

import com.hostfully.booking.exception.InvalidDateException;
import com.hostfully.booking.exception.NotFoundException;
import com.hostfully.booking.model.Block;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.request.CreateBlockRequest;
import com.hostfully.booking.repository.IBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

  private final IBlockRepository blockRepository;

  private final ValidationService validationService;
  
  public Block findById(Long id) {
    return blockRepository.findById(id).orElseThrow(NotFoundException::new);
  }
  
  public Long create(CreateBlockRequest request) {
    if (Boolean.FALSE.equals(
        validationService.sanityCheck(request.getStartDate(), request.getEndDate()))) {
      throw new InvalidDateException();
    }

    Block block =
        new Block(null, request.getPropertyId(), request.getStartDate(), request.getEndDate());
    return blockRepository.save(block).getId();
  }

  public void delete(Long id) {
    blockRepository.findById(id).orElseThrow(NotFoundException::new);
    blockRepository.deleteById(id);
  }
}
