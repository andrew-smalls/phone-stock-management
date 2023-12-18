import models.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* TODO List:
* 1. Add validation for duplicate brands
* 2. Add validation for duplicate models
* 3. Use constants for repeated strings
* 4. Use constants for repeated integers
* 5. Admin password should be stored in a file, not in the code
* */

public class Instructions {
    public static final String ADMIN_PASSWORD = "admin";
    private final Inventory inventory = new Inventory();
    private final Transactions transactions = new Transactions();
    private final ResellerRegistry resellerRegistry = new ResellerRegistry(inventory);
    private final ValidationUtils validationUtils = new ValidationUtils();
    public void list() {
        System.out.println("Executing list");
        inventory.getInventoryOverview();
    }

    public void add(String[] arguments) {
        System.out.println("Executing add" + Arrays.toString(arguments));
        if (!validationUtils.validAddArguments(arguments)) {
            return;
        }

        String brandName = arguments[0];
        String modelName = arguments[1];
        int stock = Integer.parseInt(arguments[2]);

        inventory.addBrand(brandName, modelName);
        inventory.updateStock(brandName, modelName, stock);
        transactions.logTransaction(brandName, modelName, stock, TRANSACTION_TYPE.ADD);
    }

    public void update(String[] arguments) {
        System.out.println("Executing update" + Arrays.toString(arguments));
        if (!validationUtils.validAddArguments(arguments)) {
            return;
        }

        String brandName = arguments[0];
        String modelName = arguments[1];
        int stockChange = Integer.parseInt(arguments[2]);

        PhoneModel updatedPhoneModel = inventory.updateStock(brandName, modelName, stockChange);
        if (updatedPhoneModel == null) {
            System.out.println("Update failed");
            return;
        }

        transactions.logTransaction(brandName, modelName, updatedPhoneModel.getStock(), TRANSACTION_TYPE.UPDATE);
    }


    public void search(String[] arguments) {
        System.out.println("Executing search" + Arrays.toString(arguments));
        if (!validationUtils.validSearchArguments(arguments)) {
            return;
        }

        String brandName = arguments[0];
        String modelName = arguments[1];
        PhoneModel phoneModel = inventory.search(brandName, modelName);

        System.out.println("Found in inventory:\nBrand: " + brandName + ", Model: " + phoneModel.getModelName() + ", Stock: " + phoneModel.getStock());
    }

    public void clear() {
        // Get confirmation from user: Issue an input prompt for the administrator password.
        // This does not stop the file from being processed completely.
        System.out.println("Executing clear");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the administrator password:");
        String userInput = scanner.nextLine();

        // Check if the entered password matches the admin password
        if (userInput.equals(ADMIN_PASSWORD)) {
            System.out.println("Password correct. Proceed with administrator privileges.");
            // Perform inventory clear with administrator privileges
            inventory.clear(transactions);
        } else {
            System.out.println("Incorrect password. Access denied.");
        }

        scanner.close();
    }

    public void trend() {
        System.out.println("Executing trend");

        HashMap<String, Integer> mostSoldPhoneModels = transactions.getRankingOfMostSoldPhoneModelsLastThreeMonths();
        StringBuilder stringBuilder = getFormattedTrend(mostSoldPhoneModels);
        System.out.println(stringBuilder);
    }

    private static StringBuilder getFormattedTrend(HashMap<String, Integer> mostSoldPhoneModels) {
        StringBuilder stringBuilder = new StringBuilder();

        String repeatedUnderline64 = String.format("%-" + 64 + "s", "").replace(' ', '_');
        String repeatedOverline64 = String.format("%-" + 64 + "s", "").replace(' ', '‾');
        String formattedModel = String.format("%-30s", "Model"); // 30 characters wide
        String formattedPhonesSold = String.format("%-30s", "Phones sold"); // 30 characters wide
        String repeatedDash30 = String.format("%-" + 30 + "s", "").replace(' ', '-');

        stringBuilder.append(repeatedUnderline64 + '\n');
        stringBuilder.append("|").append(formattedModel).append("|").append(formattedPhonesSold).append("|\n");
        stringBuilder.append("|").append(repeatedDash30).append("|").append(repeatedDash30).append("|");

        for (Map.Entry<String, Integer> entry : mostSoldPhoneModels.entrySet()) {
                String model = entry.getKey();
                int phonesSold = entry.getValue();
                formattedModel = String.format("%-30s", model); // 30 characters wide
                formattedPhonesSold = String.format("%-30s", phonesSold); // 30 characters wide

                stringBuilder.append("\n|").append(formattedModel).append("|").append(formattedPhonesSold).append("|");
        }

        stringBuilder.append('\n' + repeatedOverline64 + '\n');
        return stringBuilder;
    }

    public void history() {
        System.out.println("Executing history");
        transactions.showTransactions();
    }

    public void listResellers() {
        System.out.println("Executing list resellers");
        resellerRegistry.showResellers();
    }

    public void addReseller(String[] arguments) {
        System.out.println("Executing add reseller" + Arrays.toString(arguments));
        if (!validationUtils.validAddResellerArguments(arguments)) {
            System.out.println("Invalid arguments");
            return;
        }
        resellerRegistry.addReseller(arguments);
    }

    public void deleteReseller(String[] arguments) {
        System.out.println("Executing delete reseller" + Arrays.toString(arguments));
        if (!validationUtils.validDeleteResellerArguments(arguments)) {
            System.out.println("Invalid arguments");
            return;
        }
        resellerRegistry.deleteReseller(arguments);
    }

    public void assignPhone(String[] arguments) {
        System.out.println("Executing assign phone" + Arrays.toString(arguments));
        if (!validationUtils.validAssignPhoneArguments(arguments)) {
            System.out.println("Invalid arguments");
            return;
        }

        resellerRegistry.assignPhone(arguments, transactions);
    }

    public void deductStock(String[] arguments) {
        System.out.println("Executing deduct stock" + Arrays.toString(arguments));
        if (!validationUtils.validAssignPhoneArguments(arguments)) {
            System.out.println("Invalid arguments");
            return;
        }

        resellerRegistry.deductStock(arguments, transactions);
    }
}
