package com.example.logreader.persistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
  @Id
  private String eventId;
  private Long duration;
  private String type;
  private String host;
  private boolean alert;
}
