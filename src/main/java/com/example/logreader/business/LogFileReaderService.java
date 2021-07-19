package com.example.logreader.business;

import com.example.logreader.AppException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class LogFileReaderService {
  public List<LogRowDTO> read(String path) {
    final List<LogRowDTO> logs = new ArrayList();
    Path location = Paths.get(path);
    try {
      Stream<String> lines = Files.lines(location);
      lines.forEach(s -> {
        ObjectMapper mapper = new ObjectMapper();
        try {
          logs.add(mapper.readValue(s, LogRowDTO.class));
        } catch (JsonProcessingException e) {
          String message = "Fatal: invalid row format at " + s;
          log.error(message);
          throw new AppException(message);
        }
      });
      lines.close();
    } catch (IOException e) {
      String message = "Fatal: Unable to read file " + e.getMessage();
      log.error(message);
      throw new AppException(message);
    }
    return logs;
  }
}
