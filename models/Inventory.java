package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        stringBuilder.append(repeatedUnderline64).append('\n');

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

        stringBuilder.append('\n').append(repeatedOverline64).append('\n');
        System.out.println(stringBuilder);
    }

    public void addBrand(String brandName, String modelName) {
        Brand brand = createBrand(brandName, modelName, 0);

        if (getBrand(brand.getName()) != null) {
            System.out.println("Brand already exists in inventory.");
            return;
        }
        this.brands.add(brand);
    }

    public static Brand createBrand(String brandName, String modelName, int stock) {
        Brand brand = new Brand();
        brand.setName(brandName);
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.setModelName(modelName);
        phoneModel.setStock(stock);
        brand.addPhoneModel(phoneModel);
        return brand;
    }

    public PhoneModel updateStock(String brandName, String modelName, int stockChange) {
        if (!validateUpdate(brandName, modelName)) {
            return null;
        }

        PhoneModel existingPhoneModel = getBrand(brandName).getPhoneModel(modelName);
        int initialStock = existingPhoneModel.getStock();
        int finalStock = initialStock + stockChange;
        if (finalStock < 0) {
            // TODO: Specify this in the project description
            finalStock = 0;
        }
        existingPhoneModel.setStock(finalStock);
        brands.get(brands.indexOf(getBrand(brandName)))
                .getPhoneModels()
                .get(getBrand(brandName)
                        .getPhoneModels()
                        .indexOf(existingPhoneModel))
                .setStock(finalStock);

        PhoneModel updatedPhoneModel = getBrand(brandName).getPhoneModel(modelName);
        return updatedPhoneModel;
    }

    private boolean validateUpdate(String brandName, String modelName) {
        if (getBrand(brandName) == null) {
            System.out.println("Brand does not exist in inventory");
            return false;
        }

        if (getBrand(brandName).getPhoneModel(modelName) == null) {
            System.out.println("Model does not exist in inventory");
            return false;
        }
        return true;
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

    public PhoneModel search(String brandName, String modelName) {
        if (getBrand(brandName) == null) {
            System.out.println("Brand does not exist in inventory");
            return null;
        }

        PhoneModel phoneModel = getBrand(brandName).getPhoneModel(modelName);
        if (phoneModel == null) {
            System.out.println("Model does not exist in inventory");
            return null;
        }
        return phoneModel;
    }
}
