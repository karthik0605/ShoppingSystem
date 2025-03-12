package com.shopmatic;

import javafx.beans.property.*;

public class Item {
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty subcategory = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();

    public Item(String category, String subcategory, String description, double price) {
        this.category.set(category);
        this.subcategory.set(subcategory);
        this.description.set(description);
        this.price.set(price);
    }

    // Property getters for TableView binding
    public StringProperty categoryProperty() { return category; }
    public StringProperty subcategoryProperty() { return subcategory; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty priceProperty() { return price; }
}
