package core2.chapter08.demo2;

import java.io.Serializable;

public class Product implements Serializable {
    private String description;
    private double price;
    private Warehouse location;

    public Product(String description, double price){
        this.description = description;
        this.price = price;
    }

    public void setLocation(Warehouse location) {
        this.location = location;
    }

    public Warehouse getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
