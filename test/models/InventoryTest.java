package test.models;

import models.Brand;
import models.Inventory;
import models.PhoneModel;
import models.Transactions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//Also obtains 100% coverage for Brand.java
class InventoryTest {
    private static Inventory inventory;
    private static Brand brand;
    private static PhoneModel phoneModel1;
    private static PhoneModel phoneModel2;

    private static Transactions transactions;


    @BeforeAll
    static void setUp() {
        inventory = new Inventory();

        phoneModel1 = new PhoneModel();
        phoneModel1.setModelName("Model1");
        phoneModel1.setStock(10);

        phoneModel2 = new PhoneModel();
        phoneModel2.setModelName("Model2");
        phoneModel2.setStock(7);

        transactions = new Transactions();
    }

    @Test
    void addBrand() {
        //create and add new brand
        brand = new Brand();
        brand.setName("Brand1");
        inventory.addBrand(brand);
    }

    @Test
    void getBrand() {
        addBrand();
        Brand retrievedBrand = inventory.getBrand("Brand1");
        assertNotNull(retrievedBrand);
        assertEquals("Brand1", retrievedBrand.getName());
    }

    @Test
    void removeBrand() {
        //Add new brand and remove it
        Brand brand2 = new Brand();
        brand2.setName("Brand2");
        inventory.addBrand(brand2);
        Brand retrievedBrand = inventory.getBrand("Brand2");
        assertNotNull(retrievedBrand);
        assertEquals("Brand2", retrievedBrand.getName());
        inventory.removeBrand(retrievedBrand);
        assertNull(inventory.getBrand("Brand2"));
    }

    @Test
    void getStockInformationForBrand() {
        //check for empty stock
        HashMap<String, Integer> result1 = inventory.getStockInformationForBrand(brand);
        assertEquals(0, result1.size());

        //Check for 1 model existing in stock
        brand.addPhoneModel(phoneModel1);
        HashMap<String, Integer> result2 = inventory.getStockInformationForBrand(brand);
        assertEquals(1, result2.size());
        assertEquals(10, result2.get("Model1"));

        //Check for multiple models existing in stock
        brand.addPhoneModel(phoneModel2);
        HashMap<String, Integer> result3 = inventory.getStockInformationForBrand(brand);

        assertEquals(2, result3.size());
        assertEquals(10, result3.get("Model1"));
        assertEquals(7, result3.get("Model2"));
        // Verify that hasStock returns true
        assertTrue(inventory.hasStock());
    }

    @Test
    void getInventoryOverview() {
        phoneModel1.setStock(10);
        phoneModel2.setStock(7);
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        inventory.getInventoryOverview();
        // Reset System.out
        System.setOut(System.out);
        String expectedOutput = """
                ________________________________________________________________
                |Brand               |Model                         |Quantity  |
                |--------------------|------------------------------|----------|
                |Brand1              |Model1                        |10        |
                |Brand1              |Model2                        |7         |
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾

                """;
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void updateStock() {
        //Update stock for a brand that does not exist
        inventory.updateStock("Brand2", phoneModel1);
        //Update stock for a brand that exists, model doesn't exist
        inventory.updateStock("Brand1", phoneModel1);
        //Update stock for a brand that exists, model exists
        //TODO:
    }

    @Test
    void clear() {
        // Verify that the stock of all phone models is not 0
        getBrand();
        HashMap<String, Integer> result = inventory.getStockInformationForBrand(brand);
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            int stock = entry.getValue();
            assertNotEquals(0, stock);
        }
        inventory.clear(transactions);
        // Verify that the stock of all phone models is 0
        HashMap<String, Integer> result2 = inventory.getStockInformationForBrand(brand);
        for (Map.Entry<String, Integer> entry : result2.entrySet()) {
            int stock = entry.getValue();
            assertEquals(0, stock);
        }

        // Verify that hasStock returns false
        assertFalse(inventory.hasStock());
    }
}