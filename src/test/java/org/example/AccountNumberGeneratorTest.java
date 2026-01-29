package org.example;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountNumberGeneratorTest {

    @Test
    void generateReturnsEightDigitNumber() {
        String result = AccountNumberGenerator.generate();

        assertEquals(8, result.length());
        assertTrue(result.matches("\\d{8}"));
    }
}
