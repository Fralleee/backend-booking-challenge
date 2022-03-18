package com.hostfully.booking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {

  @ExceptionHandler({Exception.class})
  public ResponseEntity<String> unhandledException(Exception ex) {
    return ResponseEntity.internalServerError().build();
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<String> handleException(NotFoundException ex) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler({InvalidDateException.class})
  public ResponseEntity<String> handleException(InvalidDateException ex) {
    return ResponseEntity.badRequest().build();
  }
}
