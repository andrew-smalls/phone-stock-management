import models.Brand;
import models.Inventory;
import models.PhoneModel;

import java.util.Arrays;

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
*
* */

public class Instructions {
    private final Inventory inventory = new Inventory();

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
        System.out.println("Executing clear");
    }

    public void trend(String[] arguments) {
        System.out.println("Executing trend" + Arrays.toString(arguments));
    }

    public void history(String[] arguments) {
        System.out.println("Executing history" + Arrays.toString(arguments));
    }

    public void listResellers() {
        System.out.println("Executing list resellers");
    }

    public void addReseller(String[] arguments) {
        System.out.println("Executing add reseller" + Arrays.toString(arguments));
    }

    public void deleteReseller(String[] arguments) {
        System.out.println("Executing delete reseller" + Arrays.toString(arguments));
    }

    public void assignPhone(String[] arguments) {
        System.out.println("Executing assign phone" + Arrays.toString(arguments));
    }

    public void deductStock(String[] arguments) {
        System.out.println("Executing deduct stock" + Arrays.toString(arguments));
    }
}
