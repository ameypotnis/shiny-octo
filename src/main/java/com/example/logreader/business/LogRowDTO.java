package com.example.logreader.business;

import lombok.Data;

@Data
public class LogRowDTO {
  private String id;
  private String state;
  private String type;
  private String host;
  private Long timestamp;
}
