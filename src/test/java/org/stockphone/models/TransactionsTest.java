package org.stockphone.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stockphone.controllers.FileProcessor;
import org.stockphone.controllers.InstructionController;
import org.stockphone.controllers.Instructions;
import org.stockphone.controllers.ValidationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TransactionsTest {
    static InstructionController instructionController;
    static Transactions transactions;
    static Inventory inventory;
    static ResellerRegistry resellerRegistry;
    static ValidationUtils validationUtils;
    static String resourcesDir = "src/test/java/resources/";
    static String filePathExistent = resourcesDir + "test_instructions.txt";
    static String filePath_deductPhones = resourcesDir + "test_instructions_reseller.txt";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeAll
    static void setUp() {
        transactions = new Transactions();
        inventory = new Inventory();
        resellerRegistry = new ResellerRegistry(inventory);
        validationUtils = new ValidationUtils();
        Instructions instructions = new Instructions(inventory, transactions, resellerRegistry, validationUtils);
        instructionController = new InstructionController(instructions);

        FileProcessor fileProcessor = new FileProcessor();
        try {
            fileProcessor.processFile(filePathExistent, instructionController);
        } catch (IOException e) {
            fail("Exception occurred before running tests: " + e.getMessage());
        }
    }
    @Test
    void showTransactions() {
        transactions.showTransactions();

        String regexPattern = "timestamp=[^,]+";
        String actualResult = outContent.toString().substring(0, outContent.toString().length() - 1);
        String replaced = actualResult.replaceAll(regexPattern, "timestamp=time"); // time will always vary depending on when the test is done
        replaced = replaced.replace("\r\n", "\n"); // replace CRLF with LF
        replaced = replaced.replace("\r", "\n"); // replace CRLF with LF

        String expectedOutput = """
                Transactions
                Transaction{timestamp=time, transactionType=ADD, brandName='Nokia', modelName='XR21', stock=10}
                Transaction{timestamp=time, transactionType=ADD, brandName='Samsung', modelName='S22', stock=5}""";

        assertEquals(expectedOutput, replaced);
    }

    @Test
    void getRankingOfMostSoldPhoneModelsLastThreeMonths() {
        FileProcessor fileProcessor = new FileProcessor();
        try {
            fileProcessor.processFile(filePath_deductPhones, instructionController);
        } catch (IOException e) {
            fail("Exception occurred before running tests: " + e.getMessage());
        }
        transactions.logTransactionManually("Apple", "iPhone15", 1, TRANSACTION_TYPE.SALE, LocalDateTime.now().minusMonths(4));
        String actualResult = transactions.getRankingOfMostSoldPhoneModelsLastThreeMonths().toString();
        String expectedOutput = "{iPhone15=4, S22=1}";
        assertEquals(expectedOutput, actualResult);
    }
}