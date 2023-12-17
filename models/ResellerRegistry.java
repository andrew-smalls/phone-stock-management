package models;

import java.util.ArrayList;
import java.util.Optional;

public class ResellerRegistry {
    private final ArrayList<Reseller> resellers = new ArrayList<>();
    private final Inventory inventory;

    public ResellerRegistry(Inventory inventory) {
        this.inventory = inventory;
    }

    public void showResellers() {
        System.out.println("Resellers");
        for (Reseller reseller : resellers) {
            System.out.println(reseller);
        }
    }

    public void addReseller(String[] arguments) {
        String resellerID = arguments[0];
        String resellerName = arguments[1];
        Reseller reseller = new Reseller();
        reseller.setId(Integer.parseInt(resellerID));
        reseller.setName(resellerName);
        if (resellers.contains(reseller)) {
            System.out.println("Reseller already exists");
            return;
        }
        resellers.add(reseller);
    }

    public void deleteReseller(String[] arguments) {
        int resellerId = Integer.parseInt(arguments[0]);
        // get reseller by id and check if it contains any stock in its inventory
        Reseller reseller = getResellerById(resellerId);
        if (reseller != null) {
            Inventory resellerInventory = reseller.getResellerInventory();
            if (resellerInventory != null && resellerInventory.hasStock()) {
                System.out.println("Reseller has stock in inventory");
                return;
            }

            resellers.remove(reseller);
            System.out.println("Reseller deleted");
            return;
        }

        System.out.println("Reseller does not exist");
    }

    public void assignPhone(String[] arguments, Transactions transactions) {
        String resellerId = arguments[0];
        String brandName = arguments[1];
        String modelName = arguments[2];
        String stock = arguments[3];
        Reseller reseller = getResellerById(Integer.parseInt(resellerId));
        if (reseller == null) {
            System.out.println("Reseller does not exist");
            return;
        }

        Brand brand = this.inventory.createBrand(brandName, modelName, Integer.parseInt(stock));
        Optional<PhoneModel> phoneModel = brand.getPhoneModels().stream()
                .filter(model -> model.getModelName().equals(modelName))
                .findFirst();

        if (phoneModel.isPresent()) {
            this.inventory.updateStockOnResell(brandName, phoneModel.get());
            reseller.getResellerInventory().addBrand(brandName, modelName, Integer.parseInt(stock));
            transactions.logTransaction(brandName, modelName, Integer.parseInt(stock), TRANSACTION_TYPE.RESELLER_RESERVE);
        }
    }

    private Reseller getResellerById(int resellerId) {
        for (Reseller reseller : resellers) {
            if (reseller.getId() == resellerId) {
                return reseller;
            }
        }
        return null;
    }

    public void deductStock(String[] arguments, Transactions transactions) {
        String resellerId = arguments[0];
        String brandName = arguments[1];
        String modelName = arguments[2];
        String stock = arguments[3];
        Reseller reseller = getResellerById(Integer.parseInt(resellerId));
        if (reseller == null) {
            System.out.println("Reseller does not exist");
            return;
        }

        Brand brand = new Brand();
        brand.setName(brandName);
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.setModelName(modelName);
        phoneModel.setStock(Integer.parseInt(stock));
        brand.addPhoneModel(phoneModel);

        reseller.getResellerInventory().updateStockOnResell(brandName, phoneModel);
        transactions.logTransaction(brandName, modelName, Integer.parseInt(stock), TRANSACTION_TYPE.SALE);
    }
}
