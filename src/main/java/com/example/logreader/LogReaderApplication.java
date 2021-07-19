package com.example.logreader;

import com.example.logreader.business.LogFileProcessorService;
import com.example.logreader.persistance.Event;
import com.example.logreader.persistance.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LogReaderApplication implements CommandLineRunner {

  @Autowired
  private LogFileProcessorService service;
  @Autowired
  private EventRepository eventRepository;


  public static void main(String[] args) {
    SpringApplication.run(LogReaderApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
		service.read(args[0]);
    Iterable<Event> all = eventRepository.findAll();
    log.info("---------------------------------");
    log.info("--------SAVED EVENTS-------------");
    log.info("---------------------------------");
    all.forEach(d -> log.info(d.toString()));
  }
}
