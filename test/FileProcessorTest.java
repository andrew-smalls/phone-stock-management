import controllers.FileProcessor;
import controllers.InstructionController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {
    static InstructionController instructionController;
    static String filePathExistent = "test/resources/test_instructions.txt";
    static String filePathNonexistent = "test/resources/test_instructions_non_existent.txt";

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
            fail("Exception occurred while testing: " + e.getMessage());
        }

        try {
            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFile(filePathNonexistent, instructionController);
            fail("Exception should have been thrown");
        } catch (IOException e) {
            assertEquals(filePathNonexistent.replace("/", "\\") + " (The system cannot find the file specified)", e.getMessage());
        }
    }
    @Test
    void validateNoArguments() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] no_args = new String[] {};
        boolean result = fileProcessor.validateArguments(no_args);
        assertFalse(result);
        assertEquals("No arguments provided", outContent.toString().trim());
    }

    @Test
    void validateNonExistentFile() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] invalid_args = new String[] {
                "test/resources/test_instructions.txt",
                "test/resources/test_instructions_non_existent.txt"
        };
        boolean result = fileProcessor.validateArguments(invalid_args);
        assertFalse(result);
        assertEquals("File does not exist: test/resources/test_instructions_non_existent.txt", outContent.toString().trim());
    }

    @Test
    void validateExistentFile() {
        FileProcessor fileProcessor = new FileProcessor();

        String[] valid_args = new String[] {
                "test/resources/test_instructions.txt"
        };
        boolean result = fileProcessor.validateArguments(valid_args);
        assertTrue(result);
    }
}