package com.example.logreader.business

import com.example.logreader.persistance.EventRepository
import spock.lang.Specification

class LogFileProcessorServiceTest extends Specification {
    def reader
    def repository
    LogFileProcessorService logFileProcessorService
    void setup() {
        reader = Mock(LogFileReaderService)
        repository = Mock(EventRepository)
        logFileProcessorService = new LogFileProcessorService(reader, repository)
        logFileProcessorService.longestValue = 4
    }

    def "should calculate difference between two events" () {
        given:
        def savedEvent
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> [new LogRowDTO(id: 'abcd', state: EventTypeEnum.STARTED, timestamp: 10),
                                         new LogRowDTO(id: 'abcd', state: EventTypeEnum.FINISHED, timestamp: 13)]
        1 * repository.save(_) >> {arguments -> savedEvent=arguments[0]}
        savedEvent.duration == 3
    }

    def "should calculate difference between two events with end first" () {
        given:
        def savedEvent
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> [new LogRowDTO(id: 'abcd', state: EventTypeEnum.FINISHED, timestamp: 13),
                                         new LogRowDTO(id: 'abcd', state: EventTypeEnum.STARTED, timestamp: 10)]
        1 * repository.save(_) >> {arguments -> savedEvent=arguments[0]}
        savedEvent.duration == 3
        !savedEvent.alert
    }

    def "should calculate difference with alert true" () {
        given:
        def savedEvent
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> [new LogRowDTO(id: 'abcd', state: EventTypeEnum.FINISHED, timestamp: 15),
                                         new LogRowDTO(id: 'abcd', state: EventTypeEnum.STARTED, timestamp: 10)]
        1 * repository.save(_) >> {arguments -> savedEvent=arguments[0]}
        savedEvent.duration == 5
        savedEvent.alert
    }

    def "should ignore difference between two events if end missing" () {
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> [new LogRowDTO(id: 'abcd', state: EventTypeEnum.STARTED, timestamp: 10)]
        0 * repository.save(_)
    }

    def "should ignore difference between two events if start missing" () {
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> [new LogRowDTO(id: 'abcd', state: EventTypeEnum.FINISHED, timestamp: 15)]
        0 * repository.save(_)
    }
}
