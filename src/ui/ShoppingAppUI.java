package src.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// (Optional) Import your DB classes if needed for placeholders:
import src.DatabaseInitializer;
import src.ui.Tuples.CartedItem;
import src.ui.Tuples.Purchase;
import src.ui.queries.CustomerQueries;
import src.ui.queries.ItemQueries;
import src.ui.queries.CartedQueries;
import src.ui.queries.ReviewQueries;
import src.ui.Tuples.Item;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.ui.queries.PurchaseQueries;

/**
 * A polished, multi-tab Shopping app UI with a Home page,
 * Categories, Items, Cart, Purchases, and Reviews.
 *
 * Replace placeholders with real data logic as necessary.
 */
public class ShoppingAppUI extends Application {

    // Color palette
    private static final String GRADIENT_START = "#1565C0"; // Darker Blue
    private static final String GRADIENT_END   = "#1E88E5"; // Lighter Blue
    private static final String BACKGROUND_COLOR = "#f2f5fa";
    private static final String CARD_BACKGROUND  = "#ffffff";
    private static final String TEXT_COLOR       = "#333333";

    // Button Colors (Neutral Gray)
    private static final String BUTTON_COLOR       = "#5A5A5A";
    private static final String BUTTON_HOVER_COLOR = "#707070";
    private static final String BUTTON_TEXT_COLOR  = "#ffffff";

    private ObservableList<CartedItem> cartItems = FXCollections.observableArrayList();

    private ObservableList<Purchase> purchaseList = FXCollections.observableArrayList();

    private ObservableList<String> reviewList = FXCollections.observableArrayList();

    private String selectedCategory = null; // Track selected category
    ListView<CartedItem> cartListView = new ListView<>(cartItems);
    ListView<Purchase> purchaseListView = new ListView<>(purchaseList);

    ListView<String> reviewListView = new ListView<>(reviewList);
    int userID = -1;

    private int selectedItemID = -1; // Global variable to store selected item ID
    private TabPane tabPane; // Instance variable to store reference to TabPane



    @Override
    public void start(Stage primaryStage) {
        // Initial Home page setup
        VBox homePage = buildHomeTab();

        // Main layout with a header, center content, and footer
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Header at the top (always visible)
        mainLayout.setTop(buildHeaderBar());

        // Initially show Home page
        mainLayout.setCenter(homePage);

        // Optional Footer at the bottom
        mainLayout.setBottom(buildFooter());

        // Scene
        Scene scene = new Scene(mainLayout, 1000, 650);
        primaryStage.setTitle("ShopMatic - A Professional Shopping Experience");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    /**
     * Creates a gradient header bar with a brand label.
     */
    private HBox buildHeaderBar() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setAlignment(Pos.CENTER_LEFT);

        // Gradient background
        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(GRADIENT_START)),
                new Stop(1, Color.web(GRADIENT_END))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        header.setBackground(new Background(gradientFill));

        // Brand Name
        Label brandLabel = new Label("ShopMatic");
        brandLabel.setTextFill(Color.WHITE);
        brandLabel.setFont(Font.font("Arial", 28));

        // Slogan
        Label sloganLabel = new Label("   |   The Ultimate Shopping Experience");
        sloganLabel.setTextFill(Color.WHITE);
        sloganLabel.setFont(Font.font("Arial", 16));

        header.getChildren().addAll(brandLabel, sloganLabel);
        return header;
    }

    /**
     * Build the main tab pane.
     * We have: Home, Categories, Items, Cart, Purchases, Reviews.
     */
    private TabPane buildTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabMinHeight(40);
        tabPane.setTabMaxHeight(40);
        tabPane.setTabMinWidth(140);

        // Store the reference to TabPane
        this.tabPane = tabPane;

        // Create and add tabs to the TabPane as usual...


    // 1) Categories Tab
        Tab categoriesTab = new Tab("Categories", buildCategoriesTab());
        categoriesTab.setClosable(false);

        // 2) Items Tab
        Tab itemsTab = new Tab("Items", buildItemsTab());
        itemsTab.setClosable(false);

        // 3) Cart Tab
        Tab cartTab = new Tab("Cart", buildCartTab());
        cartTab.setClosable(false);

        // 4) Purchases Tab
        Tab purchasesTab = new Tab("Purchases", buildPurchasesTab());
        purchasesTab.setClosable(false);

        // 5) Reviews Tab
        Tab reviewsTab = new Tab("Reviews", buildReviewsTab());
        reviewsTab.setClosable(false);

        tabPane.getTabs().addAll(categoriesTab, itemsTab, cartTab, purchasesTab, reviewsTab);
        return tabPane;
    }


    /**
     * (Optional) Footer bar at bottom
     */
    private HBox buildFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(10));
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #dde3eb;");

        Label footerLabel = new Label("© 2025 ShopMatic Inc. | All Rights Reserved");
        footerLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 14;");

        footer.getChildren().add(footerLabel);
        return footer;
    }

    //===============================================================
    // 1) HOME TAB
    //   - Single input field for User ID
    //===============================================================
    private VBox buildHomeTab() {
        // Container for everything on the Home tab
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";"
                + "-fx-background-radius: 8;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        container.setAlignment(Pos.CENTER);

        // Title
        Label title = new Label("Welcome to ShopMatic!");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Info about the app
        Label info = new Label(
                "Please enter your User ID below to get started:"
        );
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        // User ID input field
        Label idLabel = new Label("User ID:");
        TextField idField = buildTextField("Enter your User ID...");

        // Set User ID button
        Button setUserIDBtn = buildButton("Set User ID");
        setUserIDBtn.setOnAction(e -> {
            String userIDStr = idField.getText().trim();
            if (!userIDStr.isEmpty()) {
                try {
                    userID = Integer.parseInt(userIDStr);  // Parse input as integer
                    System.out.println("User ID set: " + userID);  // Output to console
                    idField.clear();  // Clear input field after submission

                    // Replace the center content with the rest of the app's tabs
                    container.getScene().setRoot(buildMainAppLayout()); // Replaces Home with Tabs

                    cartItems.clear();  // Clear the existing items in the ObservableList
                    cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Please enter a valid integer ID.");
                }
            } else {
                System.out.println("Error: User ID cannot be empty.");
            }
        });

        // Lay out form
        container.getChildren().addAll(title, info, idLabel, idField, setUserIDBtn);

        return container;
    }

    private BorderPane buildMainAppLayout() {
        // Main layout with a header, center tab pane, and footer
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Header at the top (always visible)
        mainLayout.setTop(buildHeaderBar());

        // Tabs (without the Home tab)
        mainLayout.setCenter(buildTabs());

        // Optional Footer at the bottom
        mainLayout.setBottom(buildFooter());

        return mainLayout;
    }





    //===============================================================
    // 2) CATEGORIES TAB
    //   - Display category buttons: Women, Men, House Appliance, Plants, Electronics
    //   - On click, user would see items in that category (placeholder).
    //   - Provide a way to add them to cart (placeholder).
    //===============================================================
    private ScrollPane buildCategoriesTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Shop by Category");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // A flow/hbox of category buttons
        FlowPane catFlow = new FlowPane();
        catFlow.setHgap(15);
        catFlow.setVgap(15);
        catFlow.setPadding(new Insets(10));
        catFlow.setPrefWrapLength(600);

        // Example categories
        String[] categories = { "Women", "Men", "House Appliance", "Plants", "Electronics" };
        for (String cat : categories) {
            Button catBtn = buildButton(cat);
            catBtn.setMinWidth(140);
            catBtn.setOnAction(e -> {
                selectedCategory = cat;
                System.out.println("User selected category: " + cat);

                // Fetch items for the selected category and display them below the category buttons
                List<Item> items = ItemQueries.getItemsByCategoryName(cat);

                // A tile/grid of items for the selected category
                TilePane itemsGrid = new TilePane();
                itemsGrid.setHgap(20);
                itemsGrid.setVgap(20);
                itemsGrid.setPrefColumns(3); // 3 columns
                itemsGrid.setPadding(new Insets(10));

                // If no items found, display a message
                if (items.isEmpty()) {
                    Label noItemsLabel = new Label("No items found in this category.");
                    noItemsLabel.setStyle("-fx-font-size: 14; -fx-text-fill: gray;");
                    itemsGrid.getChildren().add(noItemsLabel);
                } else {
                    // Display items in the selected category
                    for (Item item : items) {
                        VBox itemCard = createItemCard(item.iname, item.iID);
                        itemsGrid.getChildren().add(itemCard);
                    }
                }

                // Clear the items grid and re-add the selected category's items
                VBox itemsContainer = (VBox) container.lookup("#itemsContainer");
                itemsContainer.getChildren().clear();
                itemsContainer.getChildren().add(itemsGrid);
            });
            catFlow.getChildren().add(catBtn);
        }

        // Info label
        Label info = new Label("Select a category above to browse products.\n"
                + "Then you can add them to your cart from the Items tab or category view.");
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        // Create a container for displaying items below the categories
        VBox itemsContainer = new VBox(10);
        itemsContainer.setId("itemsContainer");  // Set an ID to reference this container
        itemsContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8; -fx-padding: 10;");

        container.getChildren().addAll(title, catFlow, info, itemsContainer);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    //===============================================================
    // 3) ITEMS TAB
    //   - Search bar at the top
    //   - Large grid (placeholder) of items
    //   - Each item has an "Add to Cart" button
    //===============================================================
    private ScrollPane buildItemsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("All Items");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Search bar
        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search:");
        TextField searchField = buildTextField("Enter item name...");
        Button searchBtn = buildButton("Go");

        // A tile/grid of items
        TilePane itemsGrid = new TilePane();
        itemsGrid.setHgap(20);
        itemsGrid.setVgap(20);
        itemsGrid.setPrefColumns(3); // 3 columns
        itemsGrid.setPadding(new Insets(10));

        // Initial list of items to populate the grid
        List<Item> items = ItemQueries.getItems();
        for (Item item : items) {
            VBox itemCard = createItemCard(item.iname, item.iID);
            itemsGrid.getChildren().add(itemCard);
        }

        // Search button action
        searchBtn.setOnAction(e -> {
            // Clear current items in the grid
            itemsGrid.getChildren().clear();

            // Perform search query
            List<Item> itemsByName = ItemQueries.getItemsByNameSubstring(searchField.getText());
            for (Item item : itemsByName) {
                VBox itemCard = createItemCard(item.iname, item.iID);
                itemsGrid.getChildren().add(itemCard);
            }

            // Optional: Print the query being searched for
            String query = searchField.getText().trim();
            System.out.println("Searching for items with: " + query);
        });

        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getChildren().addAll(searchLabel, searchField, searchBtn);

        container.getChildren().addAll(title, searchBox, itemsGrid);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    private VBox createItemCard(String itemName, int itemID) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #f7f7f7; -fx-border-color: #dddddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPadding(new Insets(10));

        Label nameLabel = new Label(itemName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Placeholder for an image or icon
        Label imgPlaceholder = new Label("[IMG]");
        imgPlaceholder.setStyle("-fx-font-size: 24; -fx-text-fill: #999999; -fx-border-color: #cccccc; -fx-padding: 15;");

        Button addBtn = buildButton("Add to Cart");
        Button reviewBtn = buildButton("Go to Reviews");  // New button to go to Reviews

        card.setUserData(itemID);  // Store the ID in the card

        addBtn.setOnAction(e -> {
            CartedQueries.insertCartedItem(userID, itemID, 1);
            cartItems.clear();  // Clear the existing items in the ObservableList
            cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
            System.out.println(itemName + " added to cart.");
        });

        reviewBtn.setOnAction(e -> {
            // Set the selected item's ID and switch to the Reviews tab
            selectedItemID = itemID;
            System.out.print(selectedItemID);
            System.out.println("Navigating to Reviews for item ID: " + itemID);
            // Switch to the Reviews Tab
            List<String> updatedReviews = ReviewQueries.getReviewsByItem(selectedItemID);
            reviewList.clear();
            reviewList.addAll(updatedReviews);
            switchToReviewsTab();
        });


        card.getChildren().addAll(nameLabel, imgPlaceholder, addBtn, reviewBtn);
        return card;
    }

    private void switchToReviewsTab() {
        if (tabPane != null) {
            // Switch to the Reviews tab, which is the 5th tab (index 4)
            tabPane.getSelectionModel().select(4); // Reviews tab is at index 4

            // Update the Reviews tab with the selected item information
            updateReviewsTab();
        }
    }





    //===============================================================
    // 4) CART TAB
    //   - Show items user added to cart
    //   - “Purchase” button to finalize order
    //===============================================================
    private ScrollPane buildCartTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                           "-fx-background-radius: 8;" +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Your Cart");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        ListView<CartedItem> cartListView = new ListView<>(cartItems);
        cartListView.setPrefHeight(200);

        // Custom cell factory to display item details in the ListView
        cartListView.setCellFactory(lv -> new ListCell<CartedItem>() {
            @Override
            protected void updateItem(CartedItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.name + " - $" + item.price + " x" + item.quantity);
                }
            }
        });

        Button purchaseBtn = buildButton("Purchase");
        purchaseBtn.setOnAction(e -> {
            System.out.println(userID + " " + CartedQueries.getCustomerCartPrice(userID));
            PurchaseQueries.insertPurchase(userID, CartedQueries.getCustomerCartPrice(userID));
            PurchaseQueries.makePayment(userID);
            System.out.println(userID + " " + CartedQueries.getCustomerCartPrice(userID));
            cartItems.clear();  // Clear the existing items in the ObservableList
            cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
            purchaseList.clear();
            purchaseList.addAll(PurchaseQueries.getPurchases(userID));
            System.out.println("Purchasing all items in cart...");
            // Placeholder logic to move items to 'purchased'
        });

        container.getChildren().addAll(title, cartListView, purchaseBtn);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    //===============================================================
// 5) PURCHASES TAB
//   - Displays items that have already been purchased by the user
//===============================================================
    private ScrollPane buildPurchasesTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Your Purchases");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // ListView for displaying purchases, using the observable purchaseList
        ListView<Purchase> purchasesListView = new ListView<>(purchaseList);
        purchasesListView.setPrefHeight(200);
        // Custom cell factory to display purchase details in the ListView
        purchasesListView.setCellFactory(lv -> new ListCell<Purchase>() {
            @Override
            protected void updateItem(Purchase item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Displaying pid, date, and price for each purchase
                    setText("Date: " + item.date + " | Price: $" + item.price);
                }
            }
        });

        // Fetch and add the user's purchase history to the observable list
        purchaseList.clear();  // Clear existing list items
        purchaseList.addAll(PurchaseQueries.getPurchases(userID));  // Fetch purchases from DB and add to the list

        container.getChildren().addAll(title, purchasesListView);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }


    private void updateReviewsTab() {
        // If the selectedItemID is valid, update the item label in the Reviews tab
        if (selectedItemID != -1) {
            Item item = ItemQueries.getItemByID(selectedItemID);
            Label itemLabel = (Label) tabPane.lookup("#itemLabel");  // Get the itemLabel in the Reviews tab
            itemLabel.setText("Selected Item: " + item.iname);  // Update the label with the selected item name
        }
    }



    //===============================================================
    // 6) REVIEWS TAB
    //   - Shows a list of items and lets a user add a review
    //   - The review is stored (placeholder)
    //===============================================================
    // Add this import at the top

    private ScrollPane buildReviewsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Product Reviews");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Info about the selected item
        Label itemLabel = new Label("Selected Item: ");
        itemLabel.setId("itemLabel");  // Set an ID for easy access
        if (selectedItemID != -1) {
            Item item = ItemQueries.getItemByID(selectedItemID);
            itemLabel.setText("Selected Item: " + item.iname);
        }

        // Fetch reviews for the selected item
        List<String> reviews = ReviewQueries.getReviewsByItem(selectedItemID);

        // ListView for displaying reviews
        ListView<String> reviewsListView = new ListView<>();
        reviewsListView.setPrefHeight(200);

        reviewsListView.setItems(reviewList);

        // Review writing section
        Label reviewTitle = new Label("Write your review:");
        TextArea reviewArea = buildTextArea();
        reviewArea.setPromptText("Write your review here...");

        Button submitReviewButton = buildButton("Submit Review");
        submitReviewButton.setOnAction(e -> {
            String reviewText = reviewArea.getText().trim();
            if (!reviewText.isEmpty()) {
                // Insert the review into the database
                ReviewQueries.insertReview(selectedItemID, reviewText);

                // Clear the review area after submission
                reviewArea.clear();

                // Re-fetch the reviews to include the new one
                List<String> updatedReviews = ReviewQueries.getReviewsByItem(selectedItemID);

                // Use Platform.runLater() to update the ListView on the JavaFX Application Thread
                Platform.runLater(() -> {
                    reviewList.clear();
                    reviewList.addAll(updatedReviews);
                });
            } else {
                System.out.println("Review text cannot be empty.");
            }
        });

        container.getChildren().addAll(title, itemLabel, reviewsListView, reviewTitle, reviewArea, submitReviewButton);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }





    //===============================================================
    // Helper methods to style common controls
    //===============================================================
    private TextField buildTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
            "-fx-padding: 8;" +
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #d0d0d0;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-prompt-text-fill: #999999;"
        );
        return tf;
    }

    private TextArea buildTextArea() {
        TextArea ta = new TextArea();
        ta.setWrapText(true);
        ta.setStyle(
            "-fx-padding: 8;" +
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #d0d0d0;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-prompt-text-fill: #999999;"
        );
        return ta;
    }

    private Button buildButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-font-size: 14;" +
            "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
            "-fx-background-color: " + BUTTON_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-padding: 8 20;" +
            "-fx-cursor: hand;"
        );
        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-font-size: 14;" +
            "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
            "-fx-background-color: " + BUTTON_HOVER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-padding: 8 20;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-font-size: 14;" +
            "-fx-text-fill: " + BUTTON_TEXT_COLOR + ";" +
            "-fx-background-color: " + BUTTON_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-padding: 8 20;" +
            "-fx-cursor: hand;"
        ));
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
