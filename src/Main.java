package src;

import java.sql.*;

public class Main {
    static   Connection connect;
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            CustomerQueries.insertCustomer("John", "123 Street");
            CustomerQueries.getCustomers();
            CategoryQueries.insertCategory("Electronics");
            CategoryQueries.getCategories();
            ItemQueries.insertItem("IPHONE 15", 799.99, "apple iphone product", 1, 200);
            ItemQueries.getItems();
            CartedQueries.insertCartedItem(1, 1, 2);
            CartedQueries.getCartedItems();
            PurchaseQueries.insertPurchase(1);
            PurchaseQueries.getPurchases();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }}

