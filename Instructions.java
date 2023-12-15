import models.Brand;
import models.Inventory;
import models.PhoneModel;

import java.util.Arrays;

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

    public void update(String[] arguments) {
        System.out.println("Executing update" + Arrays.toString(arguments));
    }

    public void search(String[] arguments) {
        System.out.println("Executing search" + Arrays.toString(arguments));
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
