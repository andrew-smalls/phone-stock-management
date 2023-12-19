package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResellerRegistryTest {
    private static Inventory inventory;
    private static ResellerRegistry resellerRegistry;
    private static Transactions transactions;
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.addBrand("Brand_1", "Model_1");
        inventory.updateStock("Brand_1", "Model_1", 10);
        inventory.addBrand("Brand_2", "Model_2");
        inventory.updateStock("Brand_2", "Model_2", 20);
        transactions = new Transactions();
        resellerRegistry = new ResellerRegistry(inventory);
    }

    @Test
    void showResellers() {
        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        resellerRegistry.addReseller(new String[]{"2", "reseller2"});
        resellerRegistry.showResellers();

        assertTrue(outContent.toString().contains("Reseller{id=1, name='reseller1'}"));
        assertTrue(outContent.toString().contains("Reseller{id=2, name='reseller2'}"));
    }

    @Test
    void addReseller() {
        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        resellerRegistry.addReseller(new String[]{"2", "reseller2"});

        Reseller reseller1 = new Reseller();
        reseller1.setId(1);
        reseller1.setName("reseller1");
        Reseller reseller2 = new Reseller();
        reseller2.setId(2);
        reseller2.setName("reseller2");

        assertTrue(resellerRegistry.getAllResellers().contains(reseller1));
        assertTrue(resellerRegistry.getAllResellers().contains(reseller2));

        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        assertTrue(outContent.toString().contains("Reseller already exists"));
    }

    @Test
    void deleteReseller() {
        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        resellerRegistry.addReseller(new String[]{"2", "reseller2"});

        resellerRegistry.deleteReseller(new String[]{"1"});
        assertTrue(outContent.toString().contains("Reseller deleted"));

        resellerRegistry.deleteReseller(new String[]{"1"});
        assertTrue(outContent.toString().contains("Reseller ID not found"));

        resellerRegistry.assignPhone(new String[]{"2", "Brand_1", "Model_1", "5"}, transactions);
        resellerRegistry.deleteReseller(new String[]{"2"});
        assertTrue(outContent.toString().contains("Reseller has stock in inventory"));
    }

    @Test
    void assignPhone() {
        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        resellerRegistry.addReseller(new String[]{"2", "reseller2"});

        // Valid case
        resellerRegistry.assignPhone(new String[]{"1", "Brand_1", "Model_1", "5"}, transactions);
        assertTrue(inventory.getBrand("Brand_1").getPhoneModel("Model_1").getStock() == 5);
        resellerRegistry.assignPhone(new String[]{"1", "Brand_1", "Model_1", "2"}, transactions);
        assertTrue(inventory.getBrand("Brand_1").getPhoneModel("Model_1").getStock() == 3);
        transactions.showTransactions();

        String output = outContent.toString();
        String regexPattern = "timestamp=[^,]+";
        String replaced = output.replaceAll(regexPattern, "timestamp=time"); // time will always vary depending on when the test is done
        String expected = """
                Transactions
                Transaction{timestamp=time, transactionType=RESELLER_RESERVE, brandName='Brand_1', modelName='Model_1', stock=-5}
                Transaction{timestamp=time, transactionType=RESELLER_RESERVE, brandName='Brand_1', modelName='Model_1', stock=-2}    
                """;
        assertEquals(expected, replaced);

        // Invalid case: wrong id
        outContent = new ByteArrayOutputStream();
        restoreStreams();
        setUpStreams();
        resellerRegistry.assignPhone(new String[]{"0", "Brand_1", "Model_1", "5"}, transactions);
        output = outContent.toString();
        expected = "Reseller ID 0 not found. Please verify the reseller ID.\n";
        assertEquals(expected, output);

        // Invalid case: wrong brand name
        outContent = new ByteArrayOutputStream();
        restoreStreams();
        setUpStreams();
        resellerRegistry.assignPhone(new String[]{"1", "Brand_3", "Model_1", "5"}, transactions);
        output = outContent.toString();
        expected = "Brand Brand_3 not found. Please verify the brand name.\n";
        assertEquals(expected, output);

        // Invalid case: wrong model name
        outContent = new ByteArrayOutputStream();
        restoreStreams();
        setUpStreams();
        resellerRegistry.assignPhone(new String[]{"1", "Brand_1", "Model_1", "5"}, transactions);
        output = outContent.toString();
        expected = """
                Insufficient stock for Brand_1 Model_1.
                Requested: 5. Available: 3.
                """;
        assertEquals(expected, output);

        // Invalid case: insufficient stock
        outContent = new ByteArrayOutputStream();
        restoreStreams();
        setUpStreams();
        resellerRegistry.assignPhone(new String[]{"1", "Brand_1", "Model_2", "11"}, transactions);
        output = outContent.toString();
        expected = "Model Model_2 for brand Brand_1 not found. Please verify the model name.\n";
        assertEquals(expected, output);
    }

    @Test
    void deductStock() {
        resellerRegistry.addReseller(new String[]{"1", "reseller1"});
        resellerRegistry.addReseller(new String[]{"2", "reseller2"});

        // Valid case
        resellerRegistry.assignPhone(new String[]{"1", "Brand_1", "Model_1", "5"}, transactions);
        resellerRegistry.deductStock(new String[]{"1", "Brand_1", "Model_1", "2"}, transactions);
        Optional<Reseller> reseller = resellerRegistry.getAllResellers().stream().filter(r -> r.getId() == 1).findFirst();
        reseller.ifPresent(value -> assertEquals(3, value.getResellerInventory().getBrand("Brand_1").getPhoneModel("Model_1").getStock()));

        transactions.showTransactions();
        String output = outContent.toString();
        String regexPattern = "timestamp=[^,]+";
        String replaced = output.replaceAll(regexPattern, "timestamp=time"); // time will always vary depending on when the test is done
        String expected = """
                Transactions
                Transaction{timestamp=time, transactionType=RESELLER_RESERVE, brandName='Brand_1', modelName='Model_1', stock=-5}
                Transaction{timestamp=time, transactionType=SALE, brandName='Brand_1', modelName='Model_1', stock=2}
                """;
        assertEquals(expected, replaced);

        // Invalid case: wrong id
        outContent = new ByteArrayOutputStream();
        restoreStreams();
        setUpStreams();
        resellerRegistry.deductStock(new String[]{"0", "Brand_1", "Model_1", "1"}, transactions);
        output = outContent.toString();
        expected = "Reseller ID 0 not found. Please verify the reseller ID.\n";
        assertEquals(expected, output);
    }
}
