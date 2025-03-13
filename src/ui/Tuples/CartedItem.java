package src.ui.Tuples;

import src.ui.queries.CartedQueries;

public class CartedItem {
    public String name;
    public double price;
    public int quantity;
    public CartedItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
