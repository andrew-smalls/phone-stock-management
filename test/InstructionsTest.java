package test;

import controllers.Instructions;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import static controllers.Instructions.ADMIN_PASSWORD;
import static org.junit.jupiter.api.Assertions.assertEquals;


class InstructionsTest {
    private static Inventory inventory;

    private static Brand brand;

    private static PhoneModel phoneModel;

    private static Instructions instructions;

    private static Transactions transactions;
    private String[] validArguments = {"Motorola", "Moto G", "10"};


    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventory.addBrand("Samsung", "Galaxy S21");
        inventory.updateStock("Samsung", "Galaxy S21", 10);
        instructions = new Instructions();
        transactions = new Transactions();
    }

    @Test
    void list() {
        instructions.add(validArguments);
        // Redirect System.out to capture the printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.list();
        String expectedOutput = "Executing list\n" +
                "________________________________________________________________\n" +
                "|Brand               |Model                         |Quantity  |\n" +
                "|--------------------|------------------------------|----------|\n" +
                "|Motorola            |Moto G                        |10        |\n" +
                "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n" +
                "\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void add() {
        String[] invalidArguments = {"Samsung", "Galaxy S21"};
        String[] invalidArguments2 = {"Samsung", "Galaxy S21", "abc"};

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.add(validArguments);
        String expectedOutput = "Executing add" + Arrays.toString(validArguments) + "\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());

        //create new output stream to capture the printed output
        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);
        instructions.add(invalidArguments);
        String expectedOutput2 = "Executing add" + Arrays.toString(invalidArguments) + "\nInvalid number of arguments\n";
        assertEquals(expectedOutput2, outputStreamCaptor2.toString());
        instructions.add(invalidArguments);

        //create new output stream to capture the printed output
        ByteArrayOutputStream outputStreamCaptor3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor3));
        System.setOut(System.out);
        instructions.add(invalidArguments2);
        String expectedOutput3 = "Executing add" + Arrays.toString(invalidArguments2) + "\nInvalid stock argument\n";
        assertEquals(expectedOutput3, outputStreamCaptor3.toString());

    }

    @Test
    void update() {
        String[] validArguments2 = {"Motorola", "Moto G", "5"};
        String[] invalidArguments = {"Apple", "Iphone 10", "2"};

        instructions.add(validArguments);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.update(validArguments2);
        String expectedOutput1 = "Executing update" + Arrays.toString(validArguments2) + "\n";
        assertEquals(expectedOutput1, outputStreamCaptor.toString());
        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);
        instructions.update(invalidArguments);
        String expectedOutput2 = "Executing update" + Arrays.toString(invalidArguments) + "\nBrand does not exist in inventory\n" +
                "Update failed\n";
        assertEquals(expectedOutput2, outputStreamCaptor2.toString());
    }

//    @Test
//    void search() {
//
//        //TODO: andrei, vezi si tu ce nu merge aici
//        String[] validArguments = {"Motorola", "Moto G"};
//        String[] invalidArguments = {"Motorola", "Moto G", "10"};
//
//        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStreamCaptor));
//        System.setOut(System.out);
//        instructions.search(validArguments);
//        String expectedOutput = "Executing search" + Arrays.toString(validArguments) + "\n" +
//                "Found in inventory:\nBrand: Motorola, Model: Moto G, Stock: 10\n";
//        assertEquals(expectedOutput, outputStreamCaptor.toString());
//
//        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(outputStreamCaptor2));
//        System.setOut(System.out);
//        instructions.search(invalidArguments);
//        String expectedOutput2 = "Executing search" + Arrays.toString(invalidArguments) + "\n" +
//                "Invalid number of arguments\n";
//        assertEquals(expectedOutput2, outputStreamCaptor2.toString());
//    }

    @Test
    void clear() {
        String input = ADMIN_PASSWORD + "\n";
        String wrongInput = "wrong password" + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructions.clear();
        String expectedOutput = "Executing clear\n" +
                "Please enter the administrator password:\n" +
                "Password correct. Proceed with administrator privileges.\n";
        assertEquals(expectedOutput, outContent.toString());

        InputStream in2 = new ByteArrayInputStream(wrongInput.getBytes());
        System.setIn(in2);
        ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent2));
        instructions.clear();
        String expectedOutput2 = "Executing clear\n" +
                "Please enter the administrator password:\n" +
                "Incorrect password. Access denied.\n";
        assertEquals(expectedOutput2, outContent2.toString());

    }

    @Test
    void trend() {

        //TODO:
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        HashMap<String, Integer> mostSoldPhoneModels = new HashMap<>();
        mostSoldPhoneModels.put("Model1", 100);
        LocalDateTime localDateTime = LocalDateTime.now();
        TRANSACTION_TYPE transactionType = TRANSACTION_TYPE.SALE;

        transactions.logTransaction("Brand1", "Model1", 100, transactionType);
        System.setOut(System.out);
        instructions.trend();

        String actualFormattedTrend = instructions.getFormattedTrendForTesting(mostSoldPhoneModels);

        String expectedOutput = "Executing trend\n" +
                "Ranking of most sold phone models last three months\n" + actualFormattedTrend;

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void history() {
    }

    @Test
    void listResellers() {
        //TODO: Andrei

    }

    @Test
    void addReseller() {
        //TODO: Andrei

    }

    @Test
    void deleteReseller() {
        //TODO: Andrei

    }

    @Test
    void assignPhone() {
    }

    @Test
    void deductStock() {
    }
}