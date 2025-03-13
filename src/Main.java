package src;

import src.ui.queries.*;

import java.sql.*;

public class Main {
    static   Connection connect;
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            CustomerQueries.insertCustomer("John Doe", "john.doe@email.com");
            CustomerQueries.insertCustomer("Jane Smith", "jane.smith@email.com");
            CustomerQueries.insertCustomer("Michael Johnson", "michael.johnson@email.com");
            CustomerQueries.insertCustomer("Emily Davis", "emily.davis@email.com");
            CustomerQueries.insertCustomer("David Wilson", "david.wilson@email.com");
            CustomerQueries.insertCustomer("Sarah Brown", "sarah.brown@email.com");
            CustomerQueries.insertCustomer("Daniel Martinez", "daniel.martinez@email.com");
            CustomerQueries.insertCustomer("Olivia Taylor", "olivia.taylor@email.com");
            CustomerQueries.insertCustomer("James Anderson", "james.anderson@email.com");
            CustomerQueries.insertCustomer("Sophia Thomas", "sophia.thomas@email.com");


            ItemQueries.insertItem("MacBook Pro", 2399.99, "Laptop for professionals", "Electronics", 150);
            ItemQueries.insertItem("Samsung Galaxy S23", 899.99, "Latest Samsung smartphone", "Electronics", 120);
            ItemQueries.insertItem("Sony Headphones", 199.99, "Noise-canceling headphones", "Electronics", 350);

            ItemQueries.insertItem("Dyson Vacuum Cleaner", 399.99, "Powerful vacuum for home", "House Appliance", 80);
            ItemQueries.insertItem("Smart LED Bulbs", 29.99, "Set of smart LED bulbs", "House Appliance", 500);
            ItemQueries.insertItem("Washing Machine", 599.99, "Top load washing machine", "House Appliance", 60);

            ItemQueries.insertItem("Plant Pot Set", 19.99, "Set of decorative plant pots", "Plants", 250);
            ItemQueries.insertItem("Fiddle Leaf Fig Tree", 79.99, "Large indoor plant for home", "Plants", 100);
            ItemQueries.insertItem("Aloe Vera Plant", 12.99, "Aloe Vera plant for skin care", "Plants", 300);

            ItemQueries.insertItem("Nike Air Max", 129.99, "Comfortable running shoes", "Men", 100);
            ItemQueries.insertItem("Levi's Jeans", 59.99, "Classic blue denim jeans", "Men", 150);
            ItemQueries.insertItem("Casio Watch", 49.99, "Casual wristwatch for men", "Men", 200);

            ItemQueries.insertItem("Adidas UltraBoost", 180.00, "Stylish athletic shoes", "Women", 75);
            ItemQueries.insertItem("Tory Burch Handbag", 249.99, "Luxury leather handbag", "Women", 60);
            ItemQueries.insertItem("Kate Spade Wallet", 89.99, "Elegant wallet for women", "Women", 120);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }}

