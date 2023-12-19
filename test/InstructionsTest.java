package test;

import controllers.Instructions;
import models.Brand;
import models.Inventory;
import models.PhoneModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstructionsTest {
    private static Inventory inventory;

    private static Brand brand;

    private static PhoneModel phoneModel;

    @BeforeAll
    static void setUp() {
        inventory = new Inventory();
        inventory.addBrand("Samsung", "Galaxy S21");
        inventory.updateStock("Samsung", "Galaxy S21", 10);
        Instructions instructions = new Instructions();
    }

    @Test
    void list() {
        // Redirect System.out to capture the printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setOut(System.out);
        inventory.getInventoryOverview();
        String expectedOutput = "________________________________________________________________\n" +
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
        //TDOO: Andrei

    }

    @Test
    void addReseller() {
        //TDOO: Andrei

    }

    @Test
    void deleteReseller() {
        //TDOO: Andrei

    }

    @Test
    void assignPhone() {
    }

    @Test
    void deductStock() {
    }
}