package com.hostfully.booking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hostfully.booking.exception.NotFoundException;
import com.hostfully.booking.model.Block;
import com.hostfully.booking.model.Booking;
import com.hostfully.booking.model.request.CreateBlockRequest;
import com.hostfully.booking.model.request.CreateBookingRequest;
import com.hostfully.booking.model.request.UpdateBookingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingTests extends AbstractIntegrationTest {
  
  /* Get */
  @Test
  void shouldSuccessfullyRetrieveAllBookings() throws Exception {
    
    MvcResult result = mvc.perform(get("/booking")).andExpect(status().isOk()).andReturn();
    List<Booking> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() { });
  
    assertEquals(1L, response.get(0).getId());
    assertEquals(1L, response.get(0).getTenantId());
    assertEquals(1L, response.get(0).getPropertyId());
    assertEquals(LocalDate.parse("2022-02-23"), response.get(0).getStartDate());
    assertEquals(LocalDate.parse("2022-02-25"), response.get(0).getEndDate());
  
    assertEquals(2L, response.get(1).getId());
    assertEquals(2L, response.get(1).getTenantId());
    assertEquals(2L, response.get(1).getPropertyId());
    assertEquals(LocalDate.parse("2022-02-23"), response.get(1).getStartDate());
    assertEquals(LocalDate.parse("2022-02-25"), response.get(1).getEndDate());
  }
  
  @Test
  void shouldSuccessfullyRetrieveSpecificBooking() throws Exception {
    
    MvcResult result = mvc.perform(get("/booking/1")).andExpect(status().isOk()).andReturn();
    Booking response = objectMapper.readValue(result.getResponse().getContentAsString(), Booking.class);
    
    assertEquals(1L, response.getId());
    assertEquals(1L, response.getTenantId());
    assertEquals(1L, response.getPropertyId());
    assertEquals(LocalDate.parse("2022-02-23"), response.getStartDate());
    assertEquals(LocalDate.parse("2022-02-25"), response.getEndDate());
  }
  
  @Test
  void shouldFailToRetrieveSpecificBookingNotFound() throws Exception {
    
    mvc.perform(get("/booking/3")).andExpect(status().isNotFound());
  }
  
  /* Create */
  @Test
  @Transactional
  void shouldSuccessfullyCreateNewBooking() throws Exception {
    
    CreateBookingRequest request = new CreateBookingRequest();
    request.setPropertyId(1L);
    request.setTenantId(1L);
    request.setStartDate(LocalDate.now().plusMonths(1));
    request.setEndDate(LocalDate.now().plusMonths(1).plusDays(7));
    
    MvcResult result = mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
    .andReturn();
  
    Long id = objectMapper.readValue(result.getResponse().getContentAsString(), Long.class);
  
    Booking booking = bookingService.findById(id);
    
    assertEquals(id, booking.getId());
    assertEquals(1L, booking.getTenantId());
    assertEquals(1L, booking.getPropertyId());
    assertEquals(LocalDate.now().plusMonths(1), booking.getStartDate());
    assertEquals(LocalDate.now().plusMonths(1).plusDays(7), booking.getEndDate());
  }
  
  @Test
  void shouldFailToCreateNewBookingSanityCheck() throws Exception {
    
    CreateBookingRequest request = new CreateBookingRequest();
    request.setPropertyId(1L);
    request.setTenantId(1L);
    request.setStartDate(LocalDate.now().minusDays(1)); // Cannot book backwards in time
    request.setEndDate(LocalDate.now().plusDays(1));
    
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    
    request.setStartDate(LocalDate.now().plusDays(1));
    request.setEndDate(LocalDate.now().plusDays(1));  // Same day as start date
    
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  @Transactional
  void shouldFailToCreateNewBookingDatesOverlapBooking() throws Exception {
  
    CreateBookingRequest request = new CreateBookingRequest();
    request.setPropertyId(1L);
    request.setTenantId(1L);
    request.setStartDate(LocalDate.now().plusDays(10));
    request.setEndDate(LocalDate.now().plusDays(12));
  
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
  
    // Overlap on startDate
    request.setStartDate(LocalDate.now().plusDays(12));
    request.setEndDate(LocalDate.now().plusDays(14));
    
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  
    // Overlap on endDate
    request.setStartDate(LocalDate.now().plusDays(8));
    request.setEndDate(LocalDate.now().plusDays(10));
  
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  @Transactional
  void shouldFailToCreateNewBookingDatesOverlapBlock() throws Exception {
  
    CreateBlockRequest request = new CreateBlockRequest();
    request.setPropertyId(1L);
    request.setStartDate(LocalDate.now().plusDays(10));
    request.setEndDate(LocalDate.now().plusDays(12));
  
    mvc.perform(post("/booking/block")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    
    CreateBookingRequest bookingRequest = new CreateBookingRequest();
    bookingRequest.setPropertyId(1L);
    bookingRequest.setTenantId(1L);
  
    // Overlap on startDate
    request.setStartDate(LocalDate.now().plusDays(12));
    request.setEndDate(LocalDate.now().plusDays(14));
  
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  
    // Overlap on endDate
    request.setStartDate(LocalDate.now().plusDays(8));
    request.setEndDate(LocalDate.now().plusDays(10));
  
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  /* Update */
  @Test
  @Transactional
  void shouldSuccessfullyUpdateBooking() throws Exception {
  
    UpdateBookingRequest request = new UpdateBookingRequest();
    request.setStartDate(LocalDate.now().plusDays(10));
    request.setEndDate(LocalDate.now().plusDays(20));
  
    MvcResult result = mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
  
    Booking booking = bookingService.findById(1L);
  
    assertEquals(1L, booking.getId());
    assertEquals(1L, booking.getTenantId());
    assertEquals(1L, booking.getPropertyId());
    assertEquals(LocalDate.now().plusDays(10), booking.getStartDate());
    assertEquals(LocalDate.now().plusDays(20), booking.getEndDate());
  }
  
  @Test
  void shouldFailToUpdateBookingNotFound() throws Exception {
  
    UpdateBookingRequest request = new UpdateBookingRequest();
    request.setStartDate(LocalDate.now().plusDays(10));
    request.setEndDate(LocalDate.now().plusDays(20));
  
    mvc.perform(put("/booking/10")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
  }
  
  @Test
  void shouldFailToUpdateBookingDatesFailSanityCheck() throws Exception {
  
    UpdateBookingRequest request = new UpdateBookingRequest();
    request.setStartDate(LocalDate.now().minusDays(1)); // Cannot book backwards in time
    request.setEndDate(LocalDate.now().plusDays(1));
    
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    
    request.setStartDate(LocalDate.now().plusDays(1));
    request.setEndDate(LocalDate.now().plusDays(1));  // Same day as start date
    
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  @Transactional
  void shouldFailToUpdateBookingDatesOverlapBooking() throws Exception {
    
    CreateBookingRequest createRequest = new CreateBookingRequest();
    createRequest.setPropertyId(1L);
    createRequest.setTenantId(1L);
    createRequest.setStartDate(LocalDate.now().plusDays(10));
    createRequest.setEndDate(LocalDate.now().plusDays(12));
    
    mvc.perform(post("/booking")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated());
    
    UpdateBookingRequest request = new UpdateBookingRequest();
  
    // Overlap on startDate
    request.setStartDate(LocalDate.now().plusDays(12));
    request.setEndDate(LocalDate.now().plusDays(14));
    
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    
    // Overlap on endDate
    request.setStartDate(LocalDate.now().plusDays(8));
    request.setEndDate(LocalDate.now().plusDays(10));
  
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  @Transactional
  void shouldFailToUpdateBookingDatesOverlapBlock() throws Exception {
    
    CreateBlockRequest createRequest = new CreateBlockRequest();
    createRequest.setPropertyId(1L);
    createRequest.setStartDate(LocalDate.now().plusDays(10));
    createRequest.setEndDate(LocalDate.now().plusDays(12));
  
    mvc.perform(post("/booking/block")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated());
  
    UpdateBookingRequest request = new UpdateBookingRequest();
  
    // Overlap on startDate
    request.setStartDate(LocalDate.now().plusDays(12));
    request.setEndDate(LocalDate.now().plusDays(14));
  
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  
    // Overlap on endDate
    request.setStartDate(LocalDate.now().plusDays(8));
    request.setEndDate(LocalDate.now().plusDays(10));
  
    mvc.perform(put("/booking/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  /* Delete */
  @Test
  @Transactional
  void shouldSuccessfullyDeleteBooking() throws Exception {
    
    mvc.perform(delete("/booking/1")).andExpect(status().isOk());
  
    assertThrows(NotFoundException.class, () -> bookingService.findById(1L));
  }
  
  @Test
  void shouldFailToDeleteBookingNotFound() throws Exception {
    
    mvc.perform(delete("/booking/3")).andExpect(status().isNotFound());
  }
  
  /* Block */
  @Test
  @Transactional
  void shouldSuccessfullyCreateNewBlock() throws Exception {
  
    CreateBlockRequest request = new CreateBlockRequest();
    request.setPropertyId(1L);
    request.setStartDate(LocalDate.now().plusDays(1));
    request.setEndDate(LocalDate.now().plusDays(2));
    
    MvcResult result = mvc.perform(post("/booking/block")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();
    
    Long id = objectMapper.readValue(result.getResponse().getContentAsString(), Long.class);
    
    Block block = blockService.findById(id);
    
    assertEquals(id, block.getId());
    assertEquals(1L, block.getPropertyId());
    assertEquals(LocalDate.now().plusDays(1), block.getStartDate());
    assertEquals(LocalDate.now().plusDays(2), block.getEndDate());
  }
  
  @Test
  void shouldFailToCreateNewBlockSanityCheck() throws Exception {
    
    CreateBlockRequest request = new CreateBlockRequest();
    request.setPropertyId(1L);
    request.setStartDate(LocalDate.now().minusDays(1)); // Cannot book backwards in time
    request.setEndDate(LocalDate.now().plusDays(1));
    
    mvc.perform(post("/booking/block")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    
    request.setStartDate(LocalDate.now().plusDays(1));
    request.setEndDate(LocalDate.now().plusDays(1));  // Same day as start date
    
    mvc.perform(post("/booking/block")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
  }
  
  @Test
  @Transactional
  void shouldSuccessfullyDeleteBlock() throws Exception {
    
    mvc.perform(delete("/booking/block/1")).andExpect(status().isOk());
    
    assertThrows(NotFoundException.class, () -> blockService.findById(1L));
  }
  
  @Test
  void shouldFailToDeleteBlockNotFound() throws Exception {
    
    mvc.perform(delete("/booking/block/4")).andExpect(status().isNotFound());
  }
}
