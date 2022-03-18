package com.hostfully.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @JoinColumn(name = "property_id")
  private Long propertyId;

  @JoinColumn(name = "tenant_id")
  private Long tenantId;

  @Column(name = "start_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @Column(name = "end_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;
}
