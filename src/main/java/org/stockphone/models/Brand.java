package org.stockphone.models;

import java.util.ArrayList;

public class Brand {
    private String name;

    private final ArrayList<PhoneModel> phoneModels = new ArrayList<PhoneModel>();

    public Brand() {
    }

    public ArrayList<PhoneModel> getPhoneModels() {
        return phoneModels;
    }

    public void addPhoneModel(PhoneModel phoneModel) {
        this.phoneModels.add(phoneModel);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhoneModel getPhoneModel(String modelName) {
        for (PhoneModel phoneModel : phoneModels) {
            if (phoneModel.getModelName().equals(modelName)) {
                return phoneModel;
            }
        }
        return null;
    }
}
