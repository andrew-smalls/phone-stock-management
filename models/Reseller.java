package models;

public class Reseller {
    private int id;
    private String name;

    public Reseller() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reseller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}