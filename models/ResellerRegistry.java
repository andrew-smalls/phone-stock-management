package models;

import java.util.ArrayList;

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
        String resellerName = arguments[0];
        for (Reseller reseller : resellers) {
            if (reseller.getName().equals(resellerName)) {
                resellers.remove(reseller);
                return;
            }
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

        Brand brand = new Brand();
        brand.setName(brandName);
        PhoneModel phoneModel = new PhoneModel();
        phoneModel.setModelName(modelName);
        phoneModel.setStock(Integer.parseInt(stock));
        brand.addPhoneModel(phoneModel);

        this.inventory.updateStock(brandName, phoneModel);
        reseller.getResellerInventory().addBrand(brand);
        transactions.logTransaction(brandName, modelName, Integer.parseInt(stock), TRANSACTION_TYPE.RESELLER_RESERVE);
    }

    private Reseller getResellerById(int resellerId) {
        for (Reseller reseller : resellers) {
            if (reseller.getId() == resellerId) {
                return reseller;
            }
        }
        return null;
    }
}
