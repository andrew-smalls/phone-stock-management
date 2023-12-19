import controllers.Instructions;
import models.Brand;
import models.Inventory;
import models.PhoneModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InstructionsTest {
    private static Inventory inventory;

    private static Brand brand;

    private static PhoneModel phoneModel;

    private static Instructions instructions;

    @BeforeAll
    static void setUp() {
        inventory = new Inventory();
        inventory.addBrand("Samsung", "Galaxy S21");
        inventory.updateStock("Samsung", "Galaxy S21", 10);
        instructions = new Instructions();
    }

    @Test
    void list() {
        // Redirect System.out to capture the printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        instructions.list();
        String expectedOutput = "Executing list\n" +
                "________________________________________________________________\n" +
                "|Brand               |Model                         |Quantity  |\n" +
                "|--------------------|------------------------------|----------|\n" +
                "|Samsung             |Galaxy S21                    |10        |\n" +
                "‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\n" +
                "\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void add() {
        String[] validArguments = {"Samsung", "Galaxy S21", "10"};
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
    }

    @Test
    void search() {
    }

    @Test
    void clear() {
    }

    @Test
    void trend() {
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