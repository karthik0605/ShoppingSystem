package com.shopmatic;

import javafx.beans.property.*;

public class CartItem {
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();

    public CartItem(String description, double price, int quantity) {
        this.description.set(description);
        this.price.set(price);
        this.quantity.set(quantity);
    }

    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }
}
