package models;

public class PhoneModel {

    private String modelName;
    private int stock;
    public PhoneModel() {
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    @Override
    public String toString() {
        return "Phone{" +
                ", modelName='" + modelName + '\'' +
                ", stock=" + stock +
                '}';
    }
}
