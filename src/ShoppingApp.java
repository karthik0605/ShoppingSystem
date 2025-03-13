//package src;
//
//import src.ui.queries.*;
//
//import java.util.Scanner;
//
//public class ShoppingApp {
//
//    public static void main(String[] args) {
//        // 1) Make sure tables exist
//        DatabaseInitializer.createTables();
//
//        // 2) Start a simple text-based UI loop
//        Scanner scanner = new Scanner(System.in);
//        int choice = -1;
//
//        // Just an example “active user.”
//        // In a more robust app, you might do a login flow to pick the user.
//        int currentCustomerID = 0;
//
//        System.out.println("Welcome to the Shopping System!");
//
//        while (true) {
//            printMenu();
//            System.out.print("\nEnter your choice: ");
//
//            // Attempt to read the user’s choice
//            try {
//                choice = Integer.parseInt(scanner.nextLine());
//            } catch (NumberFormatException e) {
//                choice = -1;  // invalid
//            }
//
//            // Process the user’s choice
//            switch (choice) {
//                case 1:
//                    // Insert new customer
//                    System.out.print("Enter customer name: ");
//                    String name = scanner.nextLine();
//
//                    System.out.print("Enter customer address: ");
//                    String address = scanner.nextLine();
//
//                    CustomerQueries.insertCustomer(name, address);
//                    break;
//
//                case 2:
//                    // View all customers
//                    CustomerQueries.getCustomers();
//                    break;
//
//                case 3:
//                    // Create a new category
//                    System.out.print("Enter category name: ");
//                    String catName = scanner.nextLine();
//                    CategoryQueries.insertCategory(catName);
//                    break;
//
//                case 4:
//                    // Create a new item
//                    System.out.print("Enter item name: ");
//                    String itemName = scanner.nextLine();
//
//                    System.out.print("Enter item price: ");
//                    double itemPrice = Double.parseDouble(scanner.nextLine());
//
//                    System.out.print("Enter item description: ");
//                    String itemDescription = scanner.nextLine();
//
//                    System.out.print("Enter category ID: ");
//                    int catID = Integer.parseInt(scanner.nextLine());
//
//                    System.out.print("Enter item stock: ");
//                    int stock = Integer.parseInt(scanner.nextLine());
//
//                    ItemQueries.insertItem(itemName, itemPrice, itemDescription, catID, stock);
//                    break;
//
//                case 5:
//                    // View all items
//                    ItemQueries.getItems();
//                    break;
//
//                case 6:
//                    // Add item to cart
//                    System.out.print("Enter customer ID: ");
//                    currentCustomerID = Integer.parseInt(scanner.nextLine());
//
//                    System.out.print("Enter item ID to add: ");
//                    int itemID = Integer.parseInt(scanner.nextLine());
//
//                    System.out.print("Enter quantity: ");
//                    int quantity = Integer.parseInt(scanner.nextLine());
//
//                    CartedQueries.insertCartedItem(currentCustomerID, itemID, quantity);
//                    break;
//
//                case 7:
//                    // View cart items for a customer
//                    System.out.print("Enter customer ID: ");
//                    currentCustomerID = Integer.parseInt(scanner.nextLine());
//
//                    System.out.println("Items in the cart:");
//                    CartedQueries.getCustomerCartItems(currentCustomerID);
//
//                    System.out.println("Total price:");
//                    CartedQueries.getCustomerCartPrice(currentCustomerID);
//                    break;
//
//                case 8:
//                    // Make payment (which triggers stock update & clearing the cart)
//                    System.out.print("Enter customer ID: ");
//                    currentCustomerID = Integer.parseInt(scanner.nextLine());
//
//                    // Record purchase in the Purchases table
//                    // Then reduce stock from Items & clear cart
//                    PurchaseQueries.insertPurchase(currentCustomerID);
//                    PurchaseQueries.makePayment(currentCustomerID);
//                    break;
//
//                case 9:
//                    // View all purchases
//                    PurchaseQueries.getPurchases();
//                    break;
//
//                case 0:
//                    // Exit the application
//                    System.out.println("Goodbye!");
//                    scanner.close();
//                    return;
//
//                default:
//                    System.out.println("Invalid option. Please try again.");
//                    break;
//            }
//        }
//    }
//
//    // Simple helper to print the menu
//    private static void printMenu() {
//        System.out.println("\n***** Shopping System Menu *****");
//        System.out.println("1. Add a new customer");
//        System.out.println("2. View all customers");
//        System.out.println("3. Create a new category");
//        System.out.println("4. Create a new item");
//        System.out.println("5. View all items");
//        System.out.println("6. Add item to cart");
//        System.out.println("7. View cart & total price");
//        System.out.println("8. Make payment (checkout)");
//        System.out.println("9. View all purchases");
//        System.out.println("0. Exit");
//    }
//}
