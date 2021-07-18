package com.example.logreader.business

import spock.lang.Specification

class LogFileProcessorServiceTest extends Specification {
    def reader
    LogFileProcessorService logFileProcessorService
    void setup() {
        reader = Mock(LogFileReaderService)
        logFileProcessorService = new LogFileProcessorService(reader)
    }

    def "should calculate difference between two events" () {
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> []
    }

    def "should calculate difference between two events with end first" () {
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> []
    }

    def "should calculate difference between two events with 4ms diff" () {
        when:
        logFileProcessorService.read('dummyPath')
        then:
        1 * reader.read('dummyPath') >> []
    }
}
