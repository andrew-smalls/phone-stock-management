package org.stockphone;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stockphone.controllers.Instructions;
import org.stockphone.controllers.ValidationUtils;
import org.stockphone.models.Inventory;
import org.stockphone.models.ResellerRegistry;
import org.stockphone.models.Transactions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.stockphone.controllers.Instructions.ADMIN_PASSWORD;


class InstructionsTest {
    private static Inventory inventory;
    private static Instructions instructions;
    private static Transactions transactions;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        transactions = new Transactions();
        ResellerRegistry resellerRegistry = new ResellerRegistry(inventory);
        ValidationUtils validationUtils = new ValidationUtils();

        instructions = new Instructions(inventory, transactions, resellerRegistry, validationUtils);
    }

    @Test
    void testConstructor() {
        Instructions someInstructions = new Instructions();
        assertEquals(Instructions.class, someInstructions.getClass());
    }

    @Test
    void list() {
        String[] validArguments = {"Motorola", "Moto G", "10"};
        instructions.add(validArguments);

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);

        instructions.list();
        String expectedOutput = """
                Executing list
                ________________________________________________________________
                |Brand               |Model                         |Quantity  |
                |--------------------|------------------------------|----------|
                |Motorola            |Moto G                        |10        |
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                
                """;
        assertThat(outputStreamCaptor.toString()).isEqualToNormalizingNewlines(expectedOutput);
    }

    @Test
    void add() {
        String[] validArguments = {"Motorola", "Moto G", "10"};

        inventory.addBrand("Samsung", "Galaxy S21");
        inventory.updateStock("Samsung", "Galaxy S21", 10);

        String[] invalidArguments = {"Samsung", "Galaxy S21"};
        String[] invalidArguments2 = {"Samsung", "Galaxy S21", "abc"};

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.add(validArguments);
        String expectedOutput = "Executing add" + Arrays.toString(validArguments) + "\n";
        assertThat(outputStreamCaptor.toString()).isEqualToNormalizingNewlines(expectedOutput);

        //create new output stream to capture the printed output
        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);
        instructions.add(invalidArguments);
        String expectedOutput2 = "Executing add" + Arrays.toString(invalidArguments) + "\nInvalid number of arguments\n";
        assertThat(outputStreamCaptor2.toString()).isEqualToNormalizingNewlines(expectedOutput2);
        instructions.add(invalidArguments);

        //create new output stream to capture the printed output
        ByteArrayOutputStream outputStreamCaptor3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor3));
        System.setOut(System.out);
        instructions.add(invalidArguments2);
        String expectedOutput3 = "Executing add" + Arrays.toString(invalidArguments2) + "\nInvalid stock argument\n";
        assertThat(outputStreamCaptor3.toString()).isEqualToNormalizingNewlines(expectedOutput3);
    }

    @Test
    void update() {
        String[] validArguments = {"Motorola", "Moto G", "10"};
        String[] validArguments2 = {"Motorola", "Moto G", "5"};
        String[] invalidArguments = {"Motorola", "Moto G"};

        instructions.add(validArguments);
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);

        instructions.update(validArguments2);
        String expectedOutput1 = "Executing update" + Arrays.toString(validArguments2) + "\n";
        assertThat(outputStreamCaptor.toString()).isEqualToNormalizingNewlines(expectedOutput1);

        // Invalid case 1

        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);

        instructions.update(invalidArguments);
        String expectedOutput2 = """
                Executing update[Motorola, Moto G]
                Invalid number of arguments
                """;
        assertThat(outputStreamCaptor2.toString()).isEqualToNormalizingNewlines(expectedOutput2);

        // Invalid case 2
        outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);

        instructions.update(new String[]{"Motorola", "MotoNonExistent", "1"});
        expectedOutput2 = """
                Executing update[Motorola, MotoNonExistent, 1]
                Model does not exist in inventory
                Update failed
                """;
        assertThat(outputStreamCaptor2.toString()).isEqualToNormalizingNewlines(expectedOutput2);
    }

    @Test
    void search() {
        String[] existentPhone = {"Samsung", "Galaxy S21"};
        String[] nonExistentPhone = {"Motorola", "Moto G", "10"};

        inventory.addBrand("Samsung", "Galaxy S21");
        inventory.updateStock("Samsung", "Galaxy S21", 10);
        inventory.addBrand("Apple", "iPhone15");
        inventory.updateStock("Apple", "iPhone15", 5);

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.search(existentPhone);
        String expectedOutput = "Executing search" + Arrays.toString(existentPhone) + "\n" +
                "Found in inventory:\nBrand: Samsung, Model: Galaxy S21, Stock: 10\n";
        assertThat(outputStreamCaptor.toString()).isEqualToNormalizingNewlines(expectedOutput);

        ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor2));
        System.setOut(System.out);
        instructions.search(nonExistentPhone);
        String expectedOutput2 = "Executing search" + Arrays.toString(nonExistentPhone) + "\n" +
                "Invalid number of arguments\n";
        assertThat(outputStreamCaptor2.toString()).isEqualToNormalizingNewlines(expectedOutput2);
    }

    @Test
    void clear() {
        String input = ADMIN_PASSWORD + "\n";
        String wrongInput = "wrong password" + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructions.clear();
        String expectedOutput = """
                Executing clear
                Please enter the administrator password:
                Password correct. Proceed with administrator privileges.
                """;
        assertThat(outContent.toString()).isEqualToNormalizingNewlines(expectedOutput);
        InputStream in2 = new ByteArrayInputStream(wrongInput.getBytes());
        System.setIn(in2);
        ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent2));
        instructions.clear();
        String expectedOutput2 = """
                Executing clear
                Please enter the administrator password:
                Incorrect password. Access denied.
                """;
        assertThat(outContent2.toString()).isEqualToNormalizingNewlines(expectedOutput2);
    }

    @Test
    void trend() {
        instructions.add(new String[]{"Samsung", "S22", "5"});
        instructions.add(new String[]{"Apple", "iPhone15", "5"});
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});
        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "4"});
        instructions.assignPhone(new String[]{"2", "Samsung", "S22", "2"});
        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2"});
        instructions.deductStock(new String[]{"2", "Samsung", "S22", "1"});
        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2"});

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.trend();

        String expectedOutput = """
               Executing trend
               Ranking of most sold phone models last three months
               ________________________________________________________________
               |Model                         |Phones sold                   |
               |------------------------------|------------------------------|
               |iPhone15                      |4                             |
               |S22                           |1                             |
               ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                     
                """;

        String output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        HashMap<String, Integer> mostSoldPhoneModels = transactions.getRankingOfMostSoldPhoneModelsLastThreeMonths();
        expectedOutput = """
                ________________________________________________________________
                |Model                         |Phones sold                   |
                |------------------------------|------------------------------|
                |iPhone15                      |4                             |
                |S22                           |1                             |
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                """;
        assertEquals(expectedOutput, instructions.getFormattedTrendForTesting(mostSoldPhoneModels));
    }

    @Test
    void history() {
        instructions.add(new String[]{"Samsung", "S22", "5"});
        instructions.add(new String[]{"Apple", "iPhone15", "5"});
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});
        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "4"});
        instructions.assignPhone(new String[]{"2", "Samsung", "S22", "2"});
        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2"});
        instructions.deductStock(new String[]{"2", "Samsung", "S22", "1"});
        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2"});

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.history();

        String expectedOutput = """
                Executing history
                Transactions
                Transaction{timestamp=time, transactionType=ADD, brandName='Samsung', modelName='S22', stock=5}
                Transaction{timestamp=time, transactionType=ADD, brandName='Apple', modelName='iPhone15', stock=5}
                Transaction{timestamp=time, transactionType=RESELLER_RESERVE, brandName='Apple', modelName='iPhone15', stock=-4}
                Transaction{timestamp=time, transactionType=RESELLER_RESERVE, brandName='Samsung', modelName='S22', stock=-2}
                Transaction{timestamp=time, transactionType=SALE, brandName='Apple', modelName='iPhone15', stock=2}
                Transaction{timestamp=time, transactionType=SALE, brandName='Samsung', modelName='S22', stock=1}
                Transaction{timestamp=time, transactionType=SALE, brandName='Apple', modelName='iPhone15', stock=2}
                """;

        String output = outContent.toString();
        String regexPattern = "timestamp=[^,]+";
        String replaced = output.replaceAll(regexPattern, "timestamp=time");
        assertThat(replaced).isEqualToNormalizingNewlines(expectedOutput);
    }

    @Test
    void addReseller() {
        // Valid case
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.listResellers();

        String expectedOutput = """
               Executing list resellers
               Reseller{id=1, name='iStore'}
               Reseller{id=2, name='AndroidLovers'}
               """;

        String output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid case
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.addReseller(new String[]{"abc", "iStore"});
        expectedOutput = """
               Executing add reseller[abc, iStore]
               Invalid reseller id argument
               Invalid arguments
               """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.addReseller(new String[]{"1", "iStore", "abc"});
        expectedOutput = """
               Executing add reseller[1, iStore, abc]
               Invalid number of arguments
               Invalid arguments
               """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);
    }

    @Test
    void deleteReseller() {
        instructions.add(new String[]{"Samsung", "S22", "5"});
        instructions.add(new String[]{"Apple", "iPhone15", "5"});
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});

        // Valid case
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.deleteReseller(new String[]{"1"});

        String expectedOutput = """
                Executing delete reseller[1]
                Reseller deleted
                """;
        String output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid case 1
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);
        instructions.deleteReseller(new String[]{"1", "iStore"});
        expectedOutput = """
               Executing delete reseller[1, iStore]
               Invalid number of arguments
               Invalid arguments
               """;
        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid case 2
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);
        instructions.deleteReseller(new String[]{"-3"});
        expectedOutput = """
                Executing delete reseller[-3]
                Reseller id must be a positive number.
                Invalid arguments
                """;
        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid case 3
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);
        instructions.deleteReseller(new String[]{"asd"});
        expectedOutput = """
                Executing delete reseller[asd]
                Invalid reseller id argument
                Invalid arguments
                """;
        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);
    }

    @Test
    void assignPhone() {
        instructions.add(new String[]{"Samsung", "S22", "5"});
        instructions.add(new String[]{"Apple", "iPhone15", "5"});
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});

        // Valid case
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "4"});
        instructions.assignPhone(new String[]{"2", "Samsung", "S22", "2"});

        String expectedOutput = """
                Executing assign phone[1, Apple, iPhone15, 4]
                Executing assign phone[2, Samsung, S22, 2]
                """;

        String output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid  case 1
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "4", "asd"});

        expectedOutput = """
                Executing assign phone[1, Apple, iPhone15, 4, asd]
                Invalid number of arguments
                Invalid arguments
                """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid  case 2
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.assignPhone(new String[]{"asd", "Apple", "iPhone15", "4"});

        expectedOutput = """
                Executing assign phone[asd, Apple, iPhone15, 4]
                Invalid reseller id argument
                Invalid arguments
                """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid  case 3
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "-10"});

        expectedOutput = """
                Executing assign phone[1, Apple, iPhone15, -10]
                Invalid quantity. Quantity must be a positive number.
                Invalid arguments
                """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid  case 4
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "asd"});

        expectedOutput = """
                Executing assign phone[1, Apple, iPhone15, asd]
                Invalid quantity argument
                Invalid arguments
                """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);
    }

    @Test
    void deductStock() {
        instructions.add(new String[]{"Samsung", "S22", "5"});
        instructions.add(new String[]{"Apple", "iPhone15", "5"});
        instructions.addReseller(new String[]{"1", "iStore"});
        instructions.addReseller(new String[]{"2", "AndroidLovers"});
        instructions.assignPhone(new String[]{"1", "Apple", "iPhone15", "4"});
        instructions.assignPhone(new String[]{"2", "Samsung", "S22", "2"});

        // Valid case
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2"});
        instructions.deductStock(new String[]{"2", "Samsung", "S22", "1"});

        String expectedOutput = """
                Executing deduct stock[1, Apple, iPhone15, 2]
                Executing deduct stock[2, Samsung, S22, 1]
                """;

        String output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);

        // Invalid  case
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setOut(System.out);

        instructions.deductStock(new String[]{"1", "Apple", "iPhone15", "2", "asd"});

        expectedOutput = """
                Executing deduct stock[1, Apple, iPhone15, 2, asd]
                Invalid number of arguments
                Invalid arguments
                """;

        output = outContent.toString();
        assertThat(output).isEqualToNormalizingNewlines(expectedOutput);
    }
}