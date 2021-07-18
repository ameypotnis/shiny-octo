package com.example.logreader.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogFileProcessorService {

  private LogFileReaderService reader;

  @Autowired
  public LogFileProcessorService(LogFileReaderService reader) {
    this.reader = reader;
  }

  public void read(String path) {
    List<LogRowDTO> logs = reader.read(path);

  }
}
