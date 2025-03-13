package src.ui;

import javafx.application.Application;
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
import src.CustomerQueries;
import src.CategoryQueries;
import src.ItemQueries;
import src.CartedQueries;
import src.PurchaseQueries;

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

    @Override
    public void start(Stage primaryStage) {
        // Ensure DB tables exist (if your code needs it)
        DatabaseInitializer.createTables();

        // Main layout with a header, center tab pane, and optional footer
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Header at the top
        mainLayout.setTop(buildHeaderBar());

        // Tabs in the center
        mainLayout.setCenter(buildTabs());

        // (Optional) Footer at the bottom
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

        // 1) Home Tab
        Tab homeTab = new Tab("Home", buildHomeTab());
        homeTab.setClosable(false);

        // 2) Categories Tab
        Tab categoriesTab = new Tab("Categories", buildCategoriesTab());
        categoriesTab.setClosable(false);

        // 3) Items Tab
        Tab itemsTab = new Tab("Items", buildItemsTab());
        itemsTab.setClosable(false);

        // 4) Cart Tab
        Tab cartTab = new Tab("Cart", buildCartTab());
        cartTab.setClosable(false);

        // 5) Purchases Tab
        Tab purchasesTab = new Tab("Purchases", buildPurchasesTab());
        purchasesTab.setClosable(false);

        // 6) Reviews Tab
        Tab reviewsTab = new Tab("Reviews", buildReviewsTab());
        reviewsTab.setClosable(false);

        tabPane.getTabs().addAll(homeTab, categoriesTab, itemsTab, cartTab, purchasesTab, reviewsTab);
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
    //   - Info about the app
    //   - Customer name/address input (formerly in “Customers” tab)
    //===============================================================
    private ScrollPane buildHomeTab() {
        // Use a ScrollPane in case content grows
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        
        // Container for everything on the Home tab
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" 
            + "-fx-background-radius: 8;"
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        
        // Title
        Label title = new Label("Welcome to ShopMatic!");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        // Brief info about the app
        Label info = new Label(
            "ShopMatic is your one-stop solution for all your shopping needs!\n"
          + "Browse through categories, search items, add them to your cart, and purchase them.\n"
          + "You can also leave reviews for products after purchasing.\n"
          + "\nPlease enter your name and address below to get started:"
        );
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        // A small form for Name & Address
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.TOP_LEFT);

        Label nameLabel = new Label("Name:");
        TextField nameField = buildTextField("Enter customer name...");

        Label addrLabel = new Label("Address:");
        TextField addrField = buildTextField("Enter customer address...");

        TextArea outputArea = buildTextArea();

        Button addCustomerBtn = buildButton("Add Customer");
        addCustomerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String address = addrField.getText().trim();
            if (!name.isEmpty() && !address.isEmpty()) {
                // Database logic (placeholder)
                CustomerQueries.insertCustomer(name, address);
                outputArea.appendText("Customer added: " + name + "\n");
                nameField.clear();
                addrField.clear();
            } else {
                outputArea.appendText("Error: Name and address cannot be empty.\n");
            }
        });

        Button viewCustomersBtn = buildButton("View All Customers");
        viewCustomersBtn.setOnAction(e -> {
            // Prints to console in placeholder
            CustomerQueries.getCustomers();
            outputArea.appendText("[Customers printed to console.]\n");
        });

        // Lay out form
        formBox.getChildren().addAll(nameLabel, nameField, addrLabel, addrField, addCustomerBtn, viewCustomersBtn, outputArea);

        container.getChildren().addAll(title, info, formBox);

        // Wrap in a top-level VBox with padding
        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        wrapper.setAlignment(Pos.TOP_LEFT);

        scroll.setContent(wrapper);
        return scroll;
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
                // Placeholder: show items in this category, or switch to Items tab
                System.out.println("User selected category: " + cat);
            });
            catFlow.getChildren().add(catBtn);
        }

        // Info label
        Label info = new Label("Select a category above to browse products.\n"
                             + "Then you can add them to your cart from the Items tab or category view.");
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        container.getChildren().addAll(title, catFlow, info);

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
        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim();
            System.out.println("Searching for items with: " + query);
            // Placeholder for real search logic
        });
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getChildren().addAll(searchLabel, searchField, searchBtn);

        // A tile/grid of items
        TilePane itemsGrid = new TilePane();
        itemsGrid.setHgap(20);
        itemsGrid.setVgap(20);
        itemsGrid.setPrefColumns(3); // 3 columns
        itemsGrid.setPadding(new Insets(10));

        // Placeholder items
        String[] sampleItems = { "Shirt", "Laptop", "Coffee Maker", "Plant Pot", "Headphones",
                                 "Sneakers", "Mixer", "Camera", "Lamp", "Desk Chair" };
        for (String itemName : sampleItems) {
            VBox itemCard = createItemCard(itemName);
            itemsGrid.getChildren().add(itemCard);
        }

        container.getChildren().addAll(title, searchBox, itemsGrid);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    /**
     * Helper to create a small "item card" with a name, placeholder image, and "Add to Cart" button.
     */
    private VBox createItemCard(String itemName) {
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
        addBtn.setOnAction(e -> {
            // Placeholder logic
            System.out.println(itemName + " added to cart.");
        });

        card.getChildren().addAll(nameLabel, imgPlaceholder, addBtn);
        return card;
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

        // Placeholder text area or list
        TextArea cartArea = buildTextArea();
        cartArea.setPrefHeight(200);
        cartArea.setText("Items in your cart (placeholder)...\n");

        Button purchaseBtn = buildButton("Purchase");
        purchaseBtn.setOnAction(e -> {
            System.out.println("Purchasing all items in cart...");
            // Placeholder logic to move items to 'purchased'
        });

        container.getChildren().addAll(title, cartArea, purchaseBtn);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    //===============================================================
    // 5) PURCHASES TAB
    //   - Displays items that have already been purchased by past users
    //===============================================================
    private ScrollPane buildPurchasesTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";" +
                           "-fx-background-radius: 8;" +
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Purchased Items");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        TextArea purchasesArea = buildTextArea();
        purchasesArea.setPrefHeight(300);
        purchasesArea.setText("This will show all items that have been purchased by any user...\n");

        container.getChildren().addAll(title, purchasesArea);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

    //===============================================================
    // 6) REVIEWS TAB
    //   - Shows a list of items and lets a user add a review 
    //   - The review is stored (placeholder)
    //===============================================================
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

        Label info = new Label("Select an item below to add or view reviews.");
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        // Placeholder list of items
        ComboBox<String> itemSelector = new ComboBox<>();
        itemSelector.getItems().addAll("Shirt", "Laptop", "Coffee Maker", "Plant Pot", "Headphones");
        itemSelector.setPromptText("Select an item...");

        TextArea reviewArea = buildTextArea();
        reviewArea.setPromptText("Write your review here...");

        Button addReviewBtn = buildButton("Submit Review");
        addReviewBtn.setOnAction(e -> {
            String selectedItem = itemSelector.getValue();
            String reviewText = reviewArea.getText().trim();
            if (selectedItem == null || selectedItem.isEmpty()) {
                System.out.println("No item selected.");
            } else if (reviewText.isEmpty()) {
                System.out.println("No review text entered.");
            } else {
                // placeholder for DB logic
                System.out.println("Review for " + selectedItem + ": " + reviewText);
                reviewArea.clear();
            }
        });

        container.getChildren().addAll(title, info, itemSelector, reviewArea, addReviewBtn);

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
