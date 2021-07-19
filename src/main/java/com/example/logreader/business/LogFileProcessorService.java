package com.example.logreader.business;

import com.example.logreader.persistance.Event;
import com.example.logreader.persistance.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.logreader.business.EventTypeEnum.FINISHED;
import static com.example.logreader.business.EventTypeEnum.STARTED;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class LogFileProcessorService {

  private final LogFileReaderService reader;
  private final EventRepository repository;
  @Value("${duration.longestValue}")
  public long longestValue;

  @Autowired
  public LogFileProcessorService(LogFileReaderService reader, EventRepository repository) {
    this.reader = reader;
    this.repository = repository;
  }

  public void read(String path) {
    List<LogRowDTO> logs = reader.read(path);
    Map<String, List<LogRowDTO>> groupedData = logs.stream().collect(groupingBy(LogRowDTO::getId, toList()));
    groupedData.forEach((k, v) -> {
      Optional<Event> eventOptional = createEventForGivenId(k, v);
      eventOptional.ifPresent(repository::save);
    });
  }

  private Optional<Event> createEventForGivenId(String k, List<LogRowDTO> v) {
    Optional<LogRowDTO> start = v.stream().filter(logRowDTO -> STARTED.equals(logRowDTO.getState())).findFirst();
    Optional<LogRowDTO> end = v.stream().filter(logRowDTO -> FINISHED.equals(logRowDTO.getState())).findFirst();
    if (!start.isPresent() || !end.isPresent()) {
      log.warn("Start or End event missing");
      return Optional.empty();
    }
    LogRowDTO endEvent = end.get();
    LogRowDTO startEvent = start.get();
    long duration = endEvent.getTimestamp() - startEvent.getTimestamp();
    return Optional.of(
        Event
        .builder()
        .eventId(k)
        .duration(duration)
        .host(startEvent.getHost())
        .type(startEvent.getType())
        .alert(duration > longestValue)
        .build()
    );
  }
}
