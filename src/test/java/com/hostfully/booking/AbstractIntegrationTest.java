package com.hostfully.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostfully.booking.repository.IBookingRepository;
import com.hostfully.booking.service.BlockService;
import com.hostfully.booking.service.BookingService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@TestPropertySource(properties = {"spring.cache.type=NONE"})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.hostfully")
@SpringBootTest
@EnableTransactionManagement
public abstract class AbstractIntegrationTest {

  @Autowired protected MockMvc mvc;
  
  @Autowired protected ObjectMapper objectMapper;
  
  @MockBean protected RestTemplate restTemplate;
  
  @Autowired public BookingService bookingService;
  
  @Autowired public BlockService blockService;
  
}
