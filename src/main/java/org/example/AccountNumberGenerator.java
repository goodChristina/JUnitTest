package org.example;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Set<String> usedAccountNumbers = new HashSet<>();
    private static final Random random = new Random();
    private static final int MAX_ATTEMPTS = 1000;

    public static void registerExisting(String accountNumber) {
        usedAccountNumbers.add(accountNumber);
    }

    public static String generate() {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String candidate = String.format(
                    "%08d",
                    random.nextInt(100_000_000)
            );

            if (usedAccountNumbers.add(candidate)) {
                return candidate;
            }

            attempts++;
        }

        throw new IllegalStateException("Unable to generate a unique account number");
    }

    // 👇 test only, delete later
    static void resetForTest() {
        usedAccountNumbers.clear();
    }
}

