package org.stockphone;

import org.junit.jupiter.api.*;
import org.stockphone.controllers.FileProcessor;
import org.stockphone.controllers.InstructionController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class FileProcessorTest {
    static InstructionController instructionController;

    static String resourcesDir = "src/test/java/resources/";
    static String filePathExistent = resourcesDir + "test_instructions.txt";
    static String filePathNonexistent = resourcesDir + "test_instructions_non_existent.txt";

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
        instructionController = new InstructionController();

    }
    @Test
    void processFile() {
        try {
            // Call the method to be tested
            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFile(filePathExistent, instructionController);

        } catch (IOException e) {
            Assertions.fail("Exception occurred while testing: " + e.getMessage());
        }

        try {
            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFile(filePathNonexistent, instructionController);
            Assertions.fail("Exception should have been thrown");
        } catch (IOException e) {
            Assertions.assertEquals(filePathNonexistent + " (No such file or directory)", e.getMessage());
        }
    }
    @Test
    void validateNoArguments() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] no_args = new String[] {};
        boolean result = fileProcessor.validateArguments(no_args);
        Assertions.assertFalse(result);
        Assertions.assertEquals("No arguments provided", outContent.toString().trim());
    }

    @Test
    void validateNonExistentFile() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] invalid_args = new String[] {
                resourcesDir + "test_instructions.txt",
                resourcesDir + "test_instructions_non_existent.txt"
        };
        boolean result = fileProcessor.validateArguments(invalid_args);
        Assertions.assertFalse(result);
        Assertions.assertEquals("File does not exist: " + resourcesDir + "test_instructions_non_existent.txt", outContent.toString().trim());
    }

    @Test
    void validateExistentFile() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] valid_args = new String[] {
                resourcesDir + "test_instructions.txt"
        };
        boolean result = fileProcessor.validateArguments(valid_args);
        Assertions.assertTrue(result);
    }
}