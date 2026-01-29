package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaveDataToCSVTest {

    private static final String TEST_FILE = "test_bank_data.csv";

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    // basic BankSys
    static class BankSystemMinimal {
        Map<String, Customer> customerMap = new HashMap<>();

        void saveDataToCSV() throws Exception {
            try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_FILE))) {
                for (Customer customer : customerMap.values()) {
                    writer.println("C," + customer.getId() + "," + customer.getName());
                    for (Account account : customer.getAccounts().values()) {
                        String type = account instanceof IsaAccount ? "I" :
                                account instanceof BusinessAccount ? "B" : "P";
                        writer.println("A," + customer.getId() + "," + type + "," +
                                account.getAccountNumber() + "," + account.sortCode + "," +
                                String.format("%.2f", account.balance));
                    }
                }
            }
        }
    }

    @Test
    void saveDataToCSV_writesCustomerAndAccount() throws Exception {
        BankSystemMinimal bank = new BankSystemMinimal();

        // test customer and account
        Customer customer = new Customer("C1", "Alice");
        Account account = new PersonalAccount();
        account.assignIdentifiers();
        account.balance = 100.0;
        customer.addAccount(account);
        bank.customerMap.put(customer.getId(), customer);

        // start method under test
        bank.saveDataToCSV();

        // file exists
        assertTrue(Files.exists(Path.of(TEST_FILE)));

        // file contents
        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).startsWith("C,C1,Alice"));
        assertTrue(lines.get(1).startsWith("A,C1,P,"));
    }
}
