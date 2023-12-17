import models.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* TODO List:
* 1. Add validation for arguments
* 2. Add validation for integer overflow
* 3. Add validation for negative stock
* 4. Add validation for duplicate brands
* 5. Add validation for duplicate models
* 6. Add validation for non-existent brands
* 7. Add validation for non-existent models
* 8. Add validation for non-existent resellers
* 9. Refactor repeated code
* 10. Use constants for repeated strings
* 11. Use constants for repeated integers
* 12. Re-use methods when possible: update can make use of search first, for example
* 13. Admin password should be stored in a file, not in the code
* 14. Test clear
* 15. Test trend
* 16. Test history
* */

public class Instructions {
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

        inventory.addBrand(brandName, modelName, stock);
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

        String adminPassword = "admin"; // Replace this with the actual admin password

        System.out.println("Please enter the administrator password:");
        String userInput = scanner.nextLine();

        // Check if the entered password matches the admin password
        if (userInput.equals(adminPassword)) {
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

        HashMap<String, Integer> mostSoldPhoneModels =  transactions.getRankingOfMostSoldPhoneModelsLastThreeMonths();
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
        System.out.println(stringBuilder);
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
        resellerRegistry.addReseller(arguments);
    }

    public void deleteReseller(String[] arguments) {
        System.out.println("Executing delete reseller" + Arrays.toString(arguments));
        resellerRegistry.deleteReseller(arguments);
    }

    public void assignPhone(String[] arguments) {
        System.out.println("Executing assign phone" + Arrays.toString(arguments));
        resellerRegistry.assignPhone(arguments, transactions);
    }

    public void deductStock(String[] arguments) {
        System.out.println("Executing deduct stock" + Arrays.toString(arguments));
        resellerRegistry.deductStock(arguments, transactions);
    }
}
