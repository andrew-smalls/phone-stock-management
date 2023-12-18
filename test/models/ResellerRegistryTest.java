package test.models;

import models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.jupiter.api.Assertions.*;

//Also obtains 100% coverage for Reseller.java
class ResellerRegistryTest {
    private static Inventory inventory;
    private static ResellerRegistry resellerRegistry;
    private static Reseller reseller1;
    private static Brand brand;
    private static PhoneModel phoneModel1;
    private static Transactions transactions;


    String[] arguments = {String.valueOf(reseller1.getId()), reseller1.getName()};
    String[] arguments2 = {String.valueOf(reseller1.getId()), reseller1.getName(), "Galaxy S21", "10"};

    @BeforeAll
    static void setUp() {
        inventory = new Inventory();

        resellerRegistry = new ResellerRegistry(inventory);

        reseller1 = new Reseller();
        reseller1.setId(1);
        reseller1.setName("Reseller1");

        transactions = new Transactions();

        phoneModel1 = new PhoneModel();
        phoneModel1.setModelName("Model1");
        phoneModel1.setStock(10);
        brand = new Brand();
        brand.setName("Brand1");
        inventory.addBrand(brand);
        brand.addPhoneModel(phoneModel1);

    }

    @Test
    void addReseller() {
        resellerRegistry.addReseller(arguments);
        assertEquals(1, resellerRegistry.resellers.size());

//        resellerRegistry.addReseller(arguments);
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
//        resellerRegistry.addReseller(arguments);
//        System.setOut(System.out);
//        String expectedErrorMessage = "Reseller already exists\n";
        //assertEquals(expectedErrorMessage, outputStream.toString());
    }

    @Test
    void showResellers() {
        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        resellerRegistry.showResellers();
        System.setOut(System.out);
        String expectedOutput = """
                Resellers
                Reseller{id=1, name='Reseller1'}
                """;
       assertEquals(expectedOutput, outputStream.toString());
    }


    @Test
    void deleteReseller() {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStream));
        //fail delete reseller
       // resellerRegistry.deleteReseller(arguments);

//        System.setOut(System.out);
//        String expectedErrorMessage = "Reseller has stock in inventory\n";
//        assertEquals(expectedErrorMessage, outputStream.toString());
//        resellerRegistry.deleteReseller(arguments);
//        //print(resellerRegistry.resellers);

//
       //assertEquals(1, resellerRegistry.resellers.size());

       //delete reseller successfully, when reseller exists and has no stock
        resellerRegistry.deductStock(arguments2, transactions);
        resellerRegistry.deleteReseller(arguments);
       assertEquals(0, resellerRegistry.resellers.size());

    }

    @Test
    void assignPhone() {
        resellerRegistry.assignPhone(arguments2, transactions);
//
//        String[] arguments3 = {String.valueOf(reseller1.getId()), brand.getName(), phoneModel1.getModelName(), String.valueOf(phoneModel1.getStock())};
//        resellerRegistry.assignPhone(arguments3, transactions);
        }

    @Test
    void deductStock() {

        //reseller does not exist
        String[] arguments4 = {"2", String.valueOf(reseller1.getName()), "Galaxy S21", "10"};
        resellerRegistry.deductStock(arguments4, transactions);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        resellerRegistry.addReseller(arguments);
        System.setOut(System.out);
        String expectedErrorMessage = "Reseller does not exist\n";
        assertEquals(expectedErrorMessage, outputStream.toString());
    }
}