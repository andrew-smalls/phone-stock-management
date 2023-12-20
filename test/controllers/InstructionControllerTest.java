package controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static controllers.Instructions.ADMIN_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;

class InstructionControllerTest {

    InstructionController instructionController;
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
        instructionController = new InstructionController();
    }
    @Test
    void executeOption() {
        executeAdd();
        executeUpdate();
        executeList();
        executeSearch();
        executeHistory();

        executeAddReseller();
        executeListResellers();
        executeAssignPhone();
        executeDeductStock();
        executeTrend();
        executeDeleteReseller();

        executeClear();

        executeNothing();
    }

    private void executeNothing() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("nothing", new String[]{});
        String output = outContent.toString();
        String expectedOutput = "Unprocessable option. Skipped: nothing\n";
        assertEquals(expectedOutput, output);
    }

    void executeAdd() {
        instructionController.executeOption("add", new String[]{"Nokia", "3310", "10"});
        String output = outContent.toString();
        String expectedOutput = "Executing add[Nokia, 3310, 10]\n";
        assertEquals(expectedOutput, output);
    }

    void executeUpdate() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("update", new String[]{"Nokia", "3310", "5"});
        String output = outContent.toString();
        String expectedOutput = "Executing update[Nokia, 3310, 5]\n";
        assertEquals(expectedOutput, output);
    }

    void executeList() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("list", new String[]{});
        String output = outContent.toString();
        String expectedOutput = """
                Executing list
                ________________________________________________________________
                |Brand               |Model                         |Quantity  |
                |--------------------|------------------------------|----------|
                |Nokia               |3310                          |15        |
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                
                """;
        assertEquals(expectedOutput, output);
    }

    void executeSearch() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("search", new String[]{"Nokia", "3310"});
        String output = outContent.toString();
        String expectedOutput = """
                Executing search[Nokia, 3310]
                Found in inventory:
                Brand: Nokia, Model: 3310, Stock: 15
                """;
        assertEquals(expectedOutput, output);
    }

    void executeHistory() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("history", new String[]{});
        String output = outContent.toString();
        String regexPattern = "timestamp=[^,]+";
        String replaced = output.replaceAll(regexPattern, "timestamp=time");

        String expectedOutput = """
                Executing history
                Transactions
                Transaction{timestamp=time, transactionType=ADD, brandName='Nokia', modelName='3310', stock=10}
                Transaction{timestamp=time, transactionType=UPDATE, brandName='Nokia', modelName='3310', stock=15}
                """;

        assertEquals(expectedOutput, replaced);
    }

    void executeAddReseller() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("add_reseller", new String[]{"1", "reseller1"});
        String output = outContent.toString();
        String expectedOutput = """
                Executing add reseller[1, reseller1]
                """;
        assertEquals(expectedOutput, output);
    }

    void executeListResellers() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("list_resellers", new String[]{});
        String output = outContent.toString();
        String expectedOutput = """
                Executing list resellers
                Reseller{id=1, name='reseller1'}
                """;
        assertEquals(expectedOutput, output);
    }

    void executeAssignPhone() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("assign_phone", new String[]{"1", "Nokia", "3310", "1"});
        String output = outContent.toString();
        String expectedOutput = """
                Executing assign phone[1, Nokia, 3310, 1]
                """;
        assertEquals(expectedOutput, output);
    }

    void executeDeductStock() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("deduct_stock", new String[]{"1", "Nokia", "3310", "1"});
        String output = outContent.toString();
        String expectedOutput = """
                Executing deduct stock[1, Nokia, 3310, 1]
                """;
        assertEquals(expectedOutput, output);
    }

    void executeTrend() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("trend", new String[]{});
        String output = outContent.toString();
        String expectedOutput = """
                Executing trend
                Ranking of most sold phone models last three months
                ________________________________________________________________
                |Model                         |Phones sold                   |
                |------------------------------|------------------------------|
                |3310                          |1                             |
                ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
                
                """;
        assertEquals(expectedOutput, output);
    }

    void executeDeleteReseller() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        instructionController.executeOption("delete_reseller", new String[]{"1"});
        String output = outContent.toString();
        String expectedOutput = """
                Executing delete reseller[1]
                Reseller deleted
                """;
        assertEquals(expectedOutput, output);
    }

    void executeClear() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        String input = ADMIN_PASSWORD + "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        instructionController.executeOption("clear", new String[]{});

        String output = outContent.toString();
        String expectedOutput = """
                Executing clear
                Please enter the administrator password:
                Password correct. Proceed with administrator privileges.
                """;
        assertEquals(expectedOutput, output);
    }
}