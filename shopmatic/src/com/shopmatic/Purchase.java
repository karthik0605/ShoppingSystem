package com.shopmatic;

import javafx.beans.property.*;

public class Purchase {
    private final IntegerProperty itemsCount = new SimpleIntegerProperty();
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();
    private final StringProperty date = new SimpleStringProperty();

    public Purchase(int itemsCount, double totalAmount, String date) {
        this.itemsCount.set(itemsCount);
        this.totalAmount.set(totalAmount);
        this.date.set(date);
    }

    public IntegerProperty itemsCountProperty() { return itemsCount; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    public StringProperty dateProperty() { return date; }
}
