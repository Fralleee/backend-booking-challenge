package com.hostfully.booking.model;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "properties")
public class Property implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;
}
