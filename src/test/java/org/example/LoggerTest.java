package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    private static final String LOG_FILE = "bank_log.txt";

    @AfterEach
    void cleanup() throws Exception {
        // this erases the log file after each test to keep tests independent
        Files.deleteIfExists(Path.of(LOG_FILE));
    }

    @Test
    void log_writesMessageWithTimestamp() throws Exception {
        String testMessage = "Test log entry";

        // acting
        Logger.log(testMessage);

        // asserting
        assertTrue(Files.exists(Path.of(LOG_FILE)), "Log file should exist");

        List<String> lines = Files.readAllLines(Path.of(LOG_FILE));
        assertEquals(1, lines.size(), "Log file should have 1 line");

        String line = lines.get(0);

        //  starts with YYYY-MM-DD HH:mm:ss
        assertTrue(line.endsWith(testMessage), "Line should end with message");
        assertTrue(line.startsWith("["), "Line should start with timestamp");
        assertTrue(line.contains("] "), "Line should contain closing timestamp bracket");
    }

    @Test
    void log_appendsMultipleMessages() throws Exception {
        Logger.log("First message");
        Logger.log("Second message");

        List<String> lines = Files.readAllLines(Path.of(LOG_FILE));
        assertEquals(2, lines.size(), "Log file should have 2 lines");
        assertTrue(lines.get(0).endsWith("First message"));
        assertTrue(lines.get(1).endsWith("Second message"));
    }
}
