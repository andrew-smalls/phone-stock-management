package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

//Gets 100% coverage for Brand.java, PhoneModel.java, and Inventory.java
class InventoryTest {

    private static Inventory inventory;
    private static Transactions transactions;

    @BeforeAll
    static void setUp() {
        inventory = new Inventory();
        transactions = new Transactions();
    }

    @Test
    void getStockInformationForBrand() {
        // Create a brand with phone models
        Brand brand = Inventory.createBrand("Samsung", "Galaxy S21", 10);
        inventory.getAllBrands().add(brand);

        assertEquals(10, inventory.getStockInformationForBrand(brand).get("Galaxy S21"));
    }

    @Test
    void getInventoryOverview() {
        // Redirect System.out to capture the printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        inventory.updateStock("Apple", "iPhone 13", 5);
        inventory.getInventoryOverview();
        System.setOut(System.out);
        String expectedOutput = "________________________________________________________________\n" +
                "|Brand               |Model                         |Quantity  |\n" +
                "|--------------------|------------------------------|----------|\n" +
                "|Apple               |iPhone 13                     |5         |\n" +
                "|Xiaomi              |Mi 11                         |0         |\n" +
                "|Sony                |Xperia 1                      |0         |\n" +
                "|Google              |Pixel 6                       |0         |\n" +
                "|OnePlus             |9 Pro                         |0         |\n" +
                "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n" +
                "\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }
    @Test
    void addBrand() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        inventory.addBrand("Apple", "iPhone 13");
        inventory.addBrand("Apple", "iPhone 13");
        System.setOut(System.out);
        String expectedOutput = "Brand already exists in inventory.\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void updateStock() {
        inventory.addBrand("Sony", "Xperia 1");
        Brand brand = inventory.getBrand("Sony");
        PhoneModel phoneModel = brand.getPhoneModel("Xperia 1");
        PhoneModel updatedPhoneModel = inventory.updateStock("Sony", "Xperia 1", 5);
        assertEquals(5, updatedPhoneModel.getStock());
        String expectedString = "Phone{modelName='Xperia 1', stock=5}";
        String actualString = updatedPhoneModel.toString();
        assertEquals(expectedString, actualString);

        // Attempt to update stock with an invalid brand
        assertNull(inventory.updateStock("InvalidBrand", "Xperia 1", 3));

        // Attempt to update stock with an invalid model
        assertNull(inventory.updateStock("Sony", "InvalidModel", 3));

        // Attempt to update stock with a negative change
        inventory.updateStock("Sony", "Xperia 1", -10);

        // Assert that the stock does not go below zero
        assertEquals(0, phoneModel.getStock());
    }

    @Test
    void getBrand() {
        // Add a brand and check if it can be retrieved
        inventory.addBrand("Motorola", "Moto G");
        assertNotNull(inventory.getBrand("Motorola"));

        // Check for a non-existing brand
        assertNull(inventory.getBrand("Nokia"));
    }

    @Test
    void clear() {
        // Add a brand and update stock
        inventory.addBrand("Google", "Pixel 6");
        inventory.updateStock("Google", "Pixel 6", 8);

        // Clear the inventory and check if the stock is set to 0
        inventory.clear(transactions);
        assertEquals(0, inventory.getBrand("Google").getPhoneModel("Pixel 6").getStock());
    }

    @Test
    void hasStock() {
        // Add a brand with non-zero stock
        inventory.addBrand("OnePlus", "9 Pro");
        inventory.updateStock("OnePlus", "9 Pro", 3);

        // Check if the inventory has stock
        assertTrue(inventory.hasStock());

        // Clear the inventory and check if it has no stock
        inventory.clear(transactions);
        assertFalse(inventory.hasStock());
    }

    @Test
    void search() {
        // Add a brand and a phone model
        inventory.addBrand("Xiaomi", "Mi 11");
        Brand brand = inventory.getBrand("Xiaomi");

        // Search for an existing model
        assertNotNull(inventory.search("Xiaomi", "Mi 11"));

        // Search for a non-existing model
        assertNull(inventory.search("Xiaomi", "Mi 10"));

        // Search for a non-existing brand
        assertNull(inventory.search("OnePlus", "9 Pro"));
    }
}
