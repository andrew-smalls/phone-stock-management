package org.stockphone.models;

import java.util.ArrayList;
import java.util.List;

public class ResellerRegistry {
    private final ArrayList<Reseller> resellers = new ArrayList<>();
    private final Inventory inventory;

    public ResellerRegistry(Inventory inventory) {
        this.inventory = inventory;
    }

    public void showResellers() {
        for (Reseller reseller : resellers) {
            System.out.println(reseller);
        }
    }

    public void addReseller(String[] arguments) {
        Reseller reseller = createReseller(arguments);
        if (resellers.contains(reseller)) {
            System.out.println("Reseller already exists");
            return;
        }
        resellers.add(reseller);
    }

    private static Reseller createReseller(String[] arguments) {
        String resellerID = arguments[0];
        String resellerName = arguments[1];
        Reseller reseller = new Reseller();
        reseller.setId(Integer.parseInt(resellerID));
        reseller.setName(resellerName);
        return reseller;
    }

    public void deleteReseller(String[] arguments) {
        int resellerId = Integer.parseInt(arguments[0]);
        // get reseller by id and check if it contains any stock in its inventory
        Reseller reseller = getResellerById(resellerId);
        if (reseller == null) {
            System.out.println("Reseller ID not found");
            return;
        }
        if (canDeleteReseller(reseller)) {
            resellers.remove(reseller);
            System.out.println("Reseller deleted");
        }
    }

    private static boolean canDeleteReseller(Reseller reseller) {
        Inventory resellerInventory = reseller.getResellerInventory();
        if (resellerInventory.hasStock()) {
            System.out.println("Reseller has stock in inventory");
            return false;
        }
        return true;
    }

    public void assignPhone(String[] arguments, Transactions transactions) {
        int resellerId = Integer.parseInt(arguments[0]);
        String brandName = arguments[1];
        String modelName = arguments[2];
        int quantity = Integer.parseInt(arguments[3]);

        if (!canAssignPhoneStockToReseller(resellerId, brandName, modelName, quantity, inventory)) {
            return;
        }

        inventory.updateStock(brandName, modelName, -quantity);

        Reseller reseller = getResellerById(resellerId);
        boolean brandNotInResellerInventory = reseller.getResellerInventory().getBrand(brandName) == null;
        if (brandNotInResellerInventory) {
            reseller.getResellerInventory().addBrand(brandName, modelName);
        }
        reseller.getResellerInventory().updateStock(brandName, modelName, quantity);

        transactions.logTransaction(brandName, modelName, -quantity, TRANSACTION_TYPE.RESELLER_RESERVE);
    }

    private boolean canAssignPhoneStockToReseller(int resellerId, String brandName, String modelName, int quantity, Inventory inventoryToCheck) {
        Reseller reseller = getResellerById(resellerId);
        if (reseller == null) {
            System.out.println("Reseller ID " + resellerId + " not found. Please verify the reseller ID.");
            return false;
        }
        // check if brand and model exists in inventory
        Brand brand = inventoryToCheck.getBrand(brandName);
        if (brand == null) {
            System.out.println("Brand " + brandName + " not found. Please verify the brand name.");
            return false;
        }
        PhoneModel phoneModel = brand.getPhoneModel(modelName);
        if (phoneModel == null) {
            System.out.println("Model " + modelName + " for brand " + brandName + " not found. Please verify the model name.");
            return false;
        }
        // check if inventory has enough stock
        if (phoneModel.getStock() < quantity) {
            System.out.println("Insufficient stock for " + brandName + " " + modelName + ".\n" +
                    "Requested: " + quantity + ". Available: " + phoneModel.getStock() + ".");
            return false;
        }

        return true;
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
        int resellerId = Integer.parseInt(arguments[0]);
        String brandName = arguments[1];
        String modelName = arguments[2];
        int quantity = Integer.parseInt(arguments[3]);

        Reseller reseller = getResellerById(resellerId);
        if (reseller == null) {
            System.out.println("Reseller ID " + resellerId + " not found. Please verify the reseller ID.");
            return;
        }

        if (!canAssignPhoneStockToReseller(resellerId, brandName, modelName, quantity, reseller.getResellerInventory())) {
            return;
        }
        reseller.getResellerInventory().updateStock(brandName, modelName, -quantity);

        transactions.logTransaction(brandName, modelName, quantity, TRANSACTION_TYPE.SALE);
    }

    public List<Reseller> getAllResellers() {
        return new ArrayList<>(resellers);
    }
}
