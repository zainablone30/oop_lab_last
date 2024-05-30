package task.oop_openended;

import javafx.beans.property.*;

public class Product {
    private IntegerProperty id;
    private StringProperty name;
    private DoubleProperty price;
    private StringProperty imageLink;

    public Product(int id, String name, double price, String imageLink) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.imageLink = new SimpleStringProperty(imageLink);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public String getImageLink() {
        return imageLink.get();
    }

    public StringProperty imageLinkProperty() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink.set(imageLink);
    }
}
