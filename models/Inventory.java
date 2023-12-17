package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: check when adding / removing from arrays
public class Inventory {

    private final ArrayList<Brand> brands = new ArrayList<>();
    public Inventory() {
    }

    public HashMap<String, Integer> getStockInformationForBrand(Brand brand) {
        HashMap<String, Integer> stocksForBrand = new HashMap<>();
        ArrayList<PhoneModel> phoneModels = brand.getPhoneModels();
        for (PhoneModel phoneModel : phoneModels) {
            stocksForBrand.put(phoneModel.getModelName(), phoneModel.getStock());
        }
        return stocksForBrand;
    }

    public void getInventoryOverview() {
        StringBuilder stringBuilder = new StringBuilder();
        String repeatedUnderline64 = String.format("%-" + 64 + "s", "").replace(' ', '_');
        String repeatedOverline64 = String.format("%-" + 64 + "s", "").replace(' ', '‾');
        stringBuilder.append(repeatedUnderline64 + '\n');

        String formattedBrand = String.format("%-20s", "Brand"); // 20 characters wide
        String formattedModel = String.format("%-30s", "Model"); // 30 characters wide
        String formattedQuantity = String.format("%-10s", "Quantity"); // 10 characters wide
        stringBuilder.append("|").append(formattedBrand).append("|").append(formattedModel).append("|").append(formattedQuantity).append("|\n");

        String repeatedDash20 = String.format("%-" + 20 + "s", "").replace(' ', '-');
        String repeatedDash30 = String.format("%-" + 30 + "s", "").replace(' ', '-');
        String repeatedDash10 = String.format("%-" + 10 + "s", "").replace(' ', '-');
        stringBuilder.append("|").append(repeatedDash20).append("|").append(repeatedDash30).append("|").append(repeatedDash10).append("|");

        for (Brand brand : brands) {
            String brandName = brand.getName();
            Map<String, Integer> stocks = getStockInformationForBrand(brand);
            for (Map.Entry<String, Integer> stock : stocks.entrySet()) {
                String model = stock.getKey();
                int quantity = stock.getValue();
                formattedBrand = String.format("%-20s", brandName); // 20 characters wide
                formattedModel = String.format("%-30s", model); // 30 characters wide
                formattedQuantity = String.format("%-10s", quantity); // 10 characters wide

                stringBuilder.append("\n|").append(formattedBrand).append("|").append(formattedModel).append("|").append(formattedQuantity).append("|");
            }
        }

        stringBuilder.append('\n' + repeatedOverline64 + '\n');
        System.out.println(stringBuilder);
    }

    public void addBrand(Brand brand) {
        this.brands.add(brand);
    }

    public void removeBrand(Brand brand) {
        this.brands.remove(brand);
    }

    public Brand getBrand(String brandName) {
        for (Brand brand : brands) {
            if (brand.getName().equals(brandName)) {
                return brand;
            }
        }
        return null;
    }

    public void clear(Transactions transactions) {
        for (Brand brand : brands) {
            ArrayList<PhoneModel> phoneModels = brand.getPhoneModels();
            for (PhoneModel phoneModel : phoneModels) {
                phoneModel.setStock(0);
                transactions.logTransaction(brand.getName(), phoneModel.getModelName(), 0, TRANSACTION_TYPE.CLEAR);
            }
        }
    }

    public void updateStock(String brandName, PhoneModel phoneModel) {
        Brand brand = getBrand(brandName);
        if (brand == null) {
            System.out.println("Brand does not exist in inventory");
            return;
        }

        PhoneModel phoneModelInInventory = brand.getPhoneModel(phoneModel.getModelName());
        if (phoneModelInInventory == null) {
            System.out.println("Model does not exist in inventory");
            return;
        }

        int currentStock = phoneModelInInventory.getStock();
        int newStock = currentStock - phoneModel.getStock();
        if (newStock < 0) {
            System.out.println("Not enough stock to assign to reseller");
            return;
        }
        phoneModelInInventory.setStock(newStock);
        brands.stream()
                .filter(brand1 -> brand1.getName().equals(brandName))
                .forEach(brand1 -> {
                    brand1.getPhoneModels().stream()
                            .filter(phoneModel1 -> phoneModel1.getModelName().equals(phoneModel.getModelName()))
                            .forEach(phoneModel1 -> phoneModel1.setStock(newStock));
                });
    }

    public boolean hasStock() {
        for (Brand brand : brands) {
            ArrayList<PhoneModel> phoneModels = brand.getPhoneModels();
            for (PhoneModel phoneModel : phoneModels) {
                if (phoneModel.getStock() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
