package org.stockphone.models;

public class Reseller {
    private int id;
    private String name;
    private final Inventory resellerInventory = new Inventory();

    public Reseller() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Inventory getResellerInventory() {
        return resellerInventory;
    }

    @Override
    public String toString() {
        return "Reseller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Reseller reseller = (Reseller) o;
        return id == reseller.id;
    }
}
