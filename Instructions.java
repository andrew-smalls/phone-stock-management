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

    public void list() {
        System.out.println("Executing list");
        inventory.getInventoryOverview();
    }

    public void add(String[] arguments) {
        validateAddArguments(arguments);
        System.out.println("Executing add" + Arrays.toString(arguments));
        String brandName = arguments[0];
        String modelName = arguments[1];
        int stock = Integer.parseInt(arguments[2]);

        Brand brand = new Brand();
        brand.setName(brandName);
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.setModelName(modelName);
        phoneModel.setStock(stock);
        brand.addPhoneModel(phoneModel);
        inventory.addBrand(brand);

        transactions.logTransaction(brandName, modelName, stock, TRANSACTION_TYPE.ADD);
    }

    private void validateAddArguments(String[] arguments) {
        //TODO: validate arguments
    }

    //TODO: check for INTEGER overflow
    public void update(String[] arguments) {
        validateUpdateArguments(arguments);
        System.out.println("Executing update" + Arrays.toString(arguments));

        String brandName = arguments[0];
        String modelName = arguments[1];
        int stockChange = Integer.parseInt(arguments[2]);

        if (inventory.getBrand(brandName) == null) {
            System.out.println("Brand does not exist in inventory");
            return;
        }

        if (inventory.getBrand(brandName).getPhoneModel(modelName) == null) {
            System.out.println("Model does not exist in inventory");
            return;
        }
        int initialStock = inventory.getBrand(brandName).getPhoneModel(modelName).getStock();
        int finalStock = initialStock + stockChange;
        if (finalStock < 0) {
            // TODO: Specify this in the project description
            finalStock = 0;
        }
        inventory.getBrand(brandName).getPhoneModel(modelName).setStock(finalStock);

        transactions.logTransaction(brandName, modelName, finalStock, TRANSACTION_TYPE.UPDATE);
    }

    private void validateUpdateArguments(String[] arguments) {
        //TODO: validate arguments
    }

    public void search(String[] arguments) {
        System.out.println("Executing search" + Arrays.toString(arguments));

        validateSearchArguments(arguments);

        String brandName = arguments[0];
        String modelName = arguments[1];

        if (inventory.getBrand(brandName) == null) {
            System.out.println("Brand does not exist in inventory");
            return;
        }

        if (inventory.getBrand(brandName).getPhoneModel(modelName) == null) {
            System.out.println("Model does not exist in inventory");
            return;
        }

        System.out.println("Found in inventory:\nBrand: " + brandName + ", Model: " + modelName + ", Stock: " + inventory.getBrand(brandName).getPhoneModel(modelName).getStock());
    }

    private void validateSearchArguments(String[] arguments) {
        //TODO: validate arguments
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
        /*
        *  delete_reseller <ResellerID>
        Removes a reseller from the system.
        Before deleting a reseller, it's important to ensure that there are no pending transactions,
        everything that can be associated with the seller. For example if a stock is linked for
        example.
        * */
        // TODO
        System.out.println("Executing delete reseller" + Arrays.toString(arguments));
    }

    public void assignPhone(String[] arguments) {
        System.out.println("Executing assign phone" + Arrays.toString(arguments));
        resellerRegistry.assignPhone(arguments, transactions);
    }

    public void deductStock(String[] arguments) {
        /*
        * deduct_stock <ResellerID> <BrandName> <ModelName> <Quantity>
        Deducts stock from the inventory when a reseller sells or dispatches a phone model.
        "dispatching" means sending or delivering the phone model to a customer. When a reseller
        dispatches a phone model, it means they are sending it to the intended recipient.
        * */
        // TODO
        System.out.println("Executing deduct stock" + Arrays.toString(arguments));
    }
}
