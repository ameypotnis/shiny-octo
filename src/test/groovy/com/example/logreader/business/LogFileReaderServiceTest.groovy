package com.example.logreader.business

import com.example.logreader.AppException
import spock.lang.Specification

class LogFileReaderServiceTest extends Specification {
    def logFileReaderService
    void setup() {
        logFileReaderService = new LogFileReaderService()
    }

    def "Should read valid log file"() {
        given:
        String str = """{"id":"scsmbstgra", "state": "STARTED", "type": "APPLICATION_LOG", "host": "12345", "timestamp":1491377495213}
{"id":"scsmbstgrb", "state": "FINISHED", "timestamp":1491377495216}""";

        def file = "LFRSTest_1_logfile.txt"
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(str);
        writer.close();
        def actual

        when:
        actual = logFileReaderService.read(file)

        then:
        actual.size == 2
        actual[0].id == 'scsmbstgra'
        actual[1].id == 'scsmbstgrb'
    }

    def "Should NOT read invalid log file"() {
        given: 'invalid second row format'
        String str = """{"id":"scsmbstgra", "state": "STARTED", "type": "APPLICATION_LOG", "host": "12345", "timestamp":1491377495213}
{"id":"scsmbstgrb", "state": "FINISHED, "timestamp":1491377495216}""";

        def file = "LFRSTest_2_logfile.txt"
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(str);
        writer.close();

        when:
        logFileReaderService.read(file)

        then:
        thrown(AppException)
    }
}
