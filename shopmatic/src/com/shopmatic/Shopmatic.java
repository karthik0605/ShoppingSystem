package com.shopmatic;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class Shopmatic extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // MenuBar (optional)
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> primaryStage.close());
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        root.setTop(menuBar);

        // Tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab homeTab = new Tab("Home", createHomeContent());
        Tab buyTab = new Tab("Buy", createBuyContent());
        Tab sellTab = new Tab("Sell", createSellContent());
        Tab cartTab = new Tab("Cart", createCartContent());
        Tab purchaseTab = new Tab("Purchase", createPurchaseContent());

        tabPane.getTabs().addAll(homeTab, buyTab, sellTab, cartTab, purchaseTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 600);

        // Optional: Load CSS if you have style.css
        // scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        

        primaryStage.setTitle("Shopmatic");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ---------- HOME TAB ----------
    private Pane createHomeContent() {
        VBox homeBox = new VBox(15);
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to Shopmatic!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Buy, Sell, and Explore the latest fashion.");
        subtitleLabel.setStyle("-fx-font-size: 16px;");

        homeBox.getChildren().addAll(welcomeLabel, subtitleLabel);
        return homeBox;
    }

    // ---------- BUY TAB ----------
    private Pane createBuyContent() {
        BorderPane buyPane = new BorderPane();
        buyPane.setPadding(new Insets(15));

        // Top: Search bar
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10));
        TextField searchField = new TextField();
        searchField.setPromptText("Search for items...");
        Button searchButton = new Button("Search");

        // TableView for items
        TableView<Item> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Item, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        TableColumn<Item, String> subcategoryCol = new TableColumn<>("Subcategory");
        subcategoryCol.setCellValueFactory(data -> data.getValue().subcategoryProperty());

        TableColumn<Item, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());

        // Add a button column to let user add items to cart
        TableColumn<Item, Void> cartCol = new TableColumn<>("Cart");
        cartCol.setCellFactory(col -> new TableCell<>() {
            private final Button addButton = new Button("Add to Cart");
            {
                addButton.setOnAction(e -> {
                    Item item = getTableView().getItems().get(getIndex());
                    // Example userId=1 for demonstration; adapt for real auth
                    addToCart(1, item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        tableView.getColumns().addAll(categoryCol, subcategoryCol, descCol, priceCol, cartCol);

        // Search button action
        searchButton.setOnAction(e -> {
            String searchText = searchField.getText().trim();
            tableView.setItems(searchItems(searchText));
        });

        // Initialize table with all items on load
        tableView.setItems(searchItems(""));

        searchBox.getChildren().addAll(searchField, searchButton);
        buyPane.setTop(searchBox);
        buyPane.setCenter(tableView);

        return buyPane;
    }

    // ---------- SELL TAB ----------
    private Pane createSellContent() {
        VBox sellBox = new VBox(15);
        sellBox.setPadding(new Insets(15));
        sellBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Sell Your Item");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category (e.g., Men's, Women's)");

        TextField subcategoryField = new TextField();
        subcategoryField.setPromptText("Subcategory (e.g., Shirts, Jeans)");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        Button sellButton = new Button("List Item");
        sellButton.setStyle("-fx-font-size: 14px;");

        // Insert item into DB when button is clicked
        sellButton.setOnAction(e -> {
            String category = categoryField.getText().trim();
            String subcategory = subcategoryField.getText().trim();
            String desc = descriptionField.getText().trim();
            String priceStr = priceField.getText().trim();

            if (category.isEmpty() || subcategory.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
                showAlert("Error", "Please fill in all fields before listing.");
                return;
            }

            try {
                double priceVal = Double.parseDouble(priceStr);
                insertItem(category, subcategory, desc, priceVal);
                showAlert("Success", "Item listed successfully!");
                // Clear fields
                categoryField.clear();
                subcategoryField.clear();
                descriptionField.clear();
                priceField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Price must be a valid number.");
            }
        });

        sellBox.getChildren().addAll(
            titleLabel,
            new Label("Category:"), categoryField,
            new Label("Subcategory:"), subcategoryField,
            new Label("Description:"), descriptionField,
            new Label("Price:"), priceField,
            sellButton
        );

        return sellBox;
    }

    // ---------- CART TAB ----------
    private Pane createCartContent() {
        BorderPane cartPane = new BorderPane();
        cartPane.setPadding(new Insets(15));

        TableView<CartItem> cartTable = new TableView<>();
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CartItem, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> data.getValue().descriptionProperty());

        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(data -> data.getValue().priceProperty().asObject());

        TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        cartTable.getColumns().addAll(descCol, priceCol, qtyCol);

        // Load cart items for userId=1
        cartTable.setItems(loadCartItems(1));

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> {
            checkoutCart(1);
            cartTable.setItems(loadCartItems(1)); // refresh cart after checkout
        });

        VBox bottomBox = new VBox(10, checkoutButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));

        cartPane.setCenter(cartTable);
        cartPane.setBottom(bottomBox);
        return cartPane;
    }

    // ---------- PURCHASE TAB ----------
    private Pane createPurchaseContent() {
        BorderPane purchasePane = new BorderPane();
        purchasePane.setPadding(new Insets(15));

        TableView<Purchase> purchaseTable = new TableView<>();
        purchaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Purchase, Integer> itemsCol = new TableColumn<>("Number of Items");
        itemsCol.setCellValueFactory(data -> data.getValue().itemsCountProperty().asObject());

        TableColumn<Purchase, Double> totalCol = new TableColumn<>("Total Amount");
        totalCol.setCellValueFactory(data -> data.getValue().totalAmountProperty().asObject());

        TableColumn<Purchase, String> dateCol = new TableColumn<>("Purchase Date");
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());

        purchaseTable.getColumns().addAll(itemsCol, totalCol, dateCol);

        // Load purchases for userId=1
        purchaseTable.setItems(loadPurchases(1));

        purchasePane.setCenter(purchaseTable);
        return purchasePane;
    }

    // ========== Database Helper Methods ==========

    // 1) Search items in the "items" table
    private ObservableList<Item> searchItems(String searchText) {
        ObservableList<Item> itemsList = FXCollections.observableArrayList();
        String sql = "SELECT category, subcategory, description, price FROM items "
                   + "WHERE description LIKE ? OR category LIKE ? OR subcategory LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String wildcard = "%" + searchText + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String cat = rs.getString("category");
                String subcat = rs.getString("subcategory");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                itemsList.add(new Item(cat, subcat, desc, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsList;
    }

    // 2) Insert a new item (Sell tab)
    private void insertItem(String category, String subcategory, String desc, double price) {
        String sql = "INSERT INTO items (seller_id, category, subcategory, description, price) "
                   + "VALUES (1, ?, ?, ?, ?)"; // Hard-coded seller_id=1 for demo

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, subcategory);
            stmt.setString(3, desc);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3) Add item to cart
    private void addToCart(int userId, Item item) {
        // You might need to fetch item_id from the DB. For simplicity, let's do it by matching description & price
        // (Better approach: store item_id in the Item object, or query it after the user picks the row.)
        String findSql = "SELECT item_id FROM items WHERE description = ? AND price = ? LIMIT 1";
        int itemId = -1;
        try (Connection conn = Database.getConnection();
             PreparedStatement findStmt = conn.prepareStatement(findSql)) {
            findStmt.setString(1, item.descriptionProperty().get());
            findStmt.setDouble(2, item.priceProperty().get());
            ResultSet rs = findStmt.executeQuery();
            if (rs.next()) {
                itemId = rs.getInt("item_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (itemId == -1) {
            showAlert("Error", "Could not find item in DB.");
            return;
        }

        String insertCartSql = "INSERT INTO cart_items (user_id, item_id, quantity) VALUES (?, ?, 1)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertCartSql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
            showAlert("Success", "Item added to cart!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4) Load cart items
    private ObservableList<CartItem> loadCartItems(int userId) {
        ObservableList<CartItem> list = FXCollections.observableArrayList();
        String sql = "SELECT c.quantity, i.description, i.price " +
                     "FROM cart_items c " +
                     "JOIN items i ON c.item_id = i.item_id " +
                     "WHERE c.user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                list.add(new CartItem(desc, price, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 5) Checkout: move cart items to purchases
    private void checkoutCart(int userId) {
        // 1) Sum up items in cart
        String sumSQL = "SELECT SUM(c.quantity) AS totalItems, SUM(i.price * c.quantity) AS totalAmount " +
                        "FROM cart_items c JOIN items i ON c.item_id = i.item_id " +
                        "WHERE c.user_id = ?";

        int totalItems = 0;
        double totalAmount = 0.0;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sumSQL)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalItems = rs.getInt("totalItems");
                totalAmount = rs.getDouble("totalAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If cart is empty, no need to proceed
        if (totalItems == 0) {
            showAlert("Info", "Your cart is empty!");
            return;
        }

        // 2) Insert a record into purchases
        String purchaseSQL = "INSERT INTO purchases (user_id, total_items, total_amount, purchase_date) " +
                             "VALUES (?, ?, ?, NOW())";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(purchaseSQL)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, totalItems);
            stmt.setDouble(3, totalAmount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 3) Clear the cart for that user
        String deleteCartSQL = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteCartSQL)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        showAlert("Success", "Checkout complete!");
    }

    // 6) Load purchase history
    private ObservableList<Purchase> loadPurchases(int userId) {
        ObservableList<Purchase> list = FXCollections.observableArrayList();
        String sql = "SELECT total_items, total_amount, purchase_date FROM purchases WHERE user_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int itemsCount = rs.getInt("total_items");
                double amount = rs.getDouble("total_amount");
                String date = rs.getString("purchase_date");
                list.add(new Purchase(itemsCount, amount, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ========== Utility Methods ==========

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Shopmatic");
        alert.setHeaderText("Shopmatic v1.0");
        alert.setContentText("A simple JavaFX & MySQL-based shopping application.\n(c) 2025 Your Name");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
