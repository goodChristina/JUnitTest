package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LoadDataFromCSVTest {

    private static final String TEST_FILE = "test_bank_data.csv";

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    // basic "BankSystem" just for laughs
    static class BankSystemMinimal {
        Map<String, Customer> customerMap = new HashMap<>();

        void loadDataFromCSV() throws Exception {
            Path path = Path.of(TEST_FILE);
            if (!Files.exists(path)) return;

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "C" -> customerMap.put(parts[1], new Customer(parts[1], parts[2]));
                    case "A" -> {
                        Customer customer = customerMap.get(parts[1]);
                        if (customer != null) {
                            Account account = new PersonalAccount(); // basic test- PersonalAccount
                            account.accountNumber = parts[3];
                            account.sortCode = parts[4];
                            account.balance = Double.parseDouble(parts[5]);
                            customer.addAccount(account);
                        }
                    }
                }
            }
        }
    }

    @Test
    void loadDataFromCSV_readsCustomerAndAccount() throws Exception {
        // make test CSV file
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_FILE))) {
            writer.println("C,C1,Christina");
            writer.println("A,C1,P,12345,60-60-60,100.00");
        }

        BankSystemMinimal bank = new BankSystemMinimal();
        bank.loadDataFromCSV();

        // see customer loaded
        assertEquals(1, bank.customerMap.size());
        Customer customer = bank.customerMap.get("C1");
        assertNotNull(customer);
        assertEquals("Christina", customer.getName());

        // account was loaded too
        assertEquals(1, customer.getAccounts().size());
        Account account = customer.getAccount("12345");
        assertNotNull(account);
        assertEquals("12345", account.getAccountNumber());
        assertEquals("60-60-60", account.sortCode);
        assertEquals(100.00, account.balance, 0.001);
    }
}
