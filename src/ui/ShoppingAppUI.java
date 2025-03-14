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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import src.ui.Tuples.CartedItem;
import src.ui.Tuples.Purchase;
import src.ui.queries.ItemQueries;
import src.ui.queries.CartedQueries;
import src.ui.queries.ReviewQueries;
import src.ui.Tuples.Item;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.ui.queries.PurchaseQueries;

public class ShoppingAppUI extends Application {

    private static final String GRADIENT_START = "#1565C0"; // Darker Blue
    private static final String GRADIENT_END   = "#1E88E5"; // Lighter Blue
    private static final String BACKGROUND_COLOR = "#f2f5fa";
    private static final String CARD_BACKGROUND  = "#ffffff";
    private static final String TEXT_COLOR       = "#333333";

    private static final String BUTTON_COLOR       = "#5A5A5A";
    private static final String BUTTON_HOVER_COLOR = "#707070";
    private static final String BUTTON_TEXT_COLOR  = "#ffffff";

    private ObservableList<CartedItem> cartItems = FXCollections.observableArrayList();

    private ObservableList<Purchase> purchaseList = FXCollections.observableArrayList();

    private ObservableList<String> reviewList = FXCollections.observableArrayList();


    int userID = -1;

    private int selectedItemID = -1;
    private TabPane tabPane;



    @Override
    public void start(Stage primaryStage) {

        VBox homePage = buildHomeTab();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        mainLayout.setTop(buildHeaderBar());

        mainLayout.setCenter(homePage);

        mainLayout.setBottom(buildFooter());

        Scene scene = new Scene(mainLayout, 1000, 650);
        primaryStage.setTitle("ShopMatic - A Professional Shopping Experience");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox buildHeaderBar() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setAlignment(Pos.CENTER_LEFT);

        BackgroundFill gradientFill = new BackgroundFill(
            new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(GRADIENT_START)),
                new Stop(1, Color.web(GRADIENT_END))
            ),
            CornerRadii.EMPTY, Insets.EMPTY
        );
        header.setBackground(new Background(gradientFill));

        Label brandLabel = new Label("ShopMatic");
        brandLabel.setTextFill(Color.WHITE);
        brandLabel.setFont(Font.font("Arial", 28));

        Label sloganLabel = new Label("   |   The Ultimate Shopping Experience");
        sloganLabel.setTextFill(Color.WHITE);
        sloganLabel.setFont(Font.font("Arial", 16));

        header.getChildren().addAll(brandLabel, sloganLabel);
        return header;
    }

    private TabPane buildTabs() {
        TabPane tabPane = new TabPane();
        tabPane.setTabMinHeight(40);
        tabPane.setTabMaxHeight(40);
        tabPane.setTabMinWidth(140);

        this.tabPane = tabPane;

        Tab categoriesTab = new Tab("Categories", buildCategoriesTab());
        categoriesTab.setClosable(false);

        Tab itemsTab = new Tab("Items", buildItemsTab());
        itemsTab.setClosable(false);

        Tab cartTab = new Tab("Cart", buildCartTab());
        cartTab.setClosable(false);

        Tab purchasesTab = new Tab("Purchases", buildPurchasesTab());
        purchasesTab.setClosable(false);

        Tab reviewsTab = new Tab("Reviews", buildReviewsTab());
        reviewsTab.setClosable(false);

        tabPane.getTabs().addAll(categoriesTab, itemsTab, cartTab, purchasesTab, reviewsTab);
        return tabPane;
    }

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

    private VBox buildHomeTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: " + CARD_BACKGROUND + ";"
                + "-fx-background-radius: 8;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        container.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to ShopMatic!");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR + ";");

        Label info = new Label(
                "Please enter your User ID below to get started:"
        );
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        Label idLabel = new Label("User ID:");
        TextField idField = buildTextField("Enter your User ID...");

        Button setUserIDBtn = buildButton("Set User ID");
        setUserIDBtn.setOnAction(e -> {
            String userIDStr = idField.getText().trim();
            if (!userIDStr.isEmpty()) {
                try {
                    userID = Integer.parseInt(userIDStr);
                    idField.clear();

                    container.getScene().setRoot(buildMainAppLayout());

                    cartItems.clear();
                    cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Please enter a valid integer ID.");
                }
            } else {
                System.out.println("Error: User ID cannot be empty.");
            }
        });

        container.getChildren().addAll(title, info, idLabel, idField, setUserIDBtn);

        return container;
    }

    private BorderPane buildMainAppLayout() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        mainLayout.setTop(buildHeaderBar());

        mainLayout.setCenter(buildTabs());

        mainLayout.setBottom(buildFooter());

        return mainLayout;
    }

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

        FlowPane catFlow = new FlowPane();
        catFlow.setHgap(15);
        catFlow.setVgap(15);
        catFlow.setPadding(new Insets(10));
        catFlow.setPrefWrapLength(600);

        String[] categories = { "Women", "Men", "House Appliance", "Plants", "Electronics" };
        for (String cat : categories) {
            Button catBtn = buildButton(cat);
            catBtn.setMinWidth(140);
            catBtn.setOnAction(e -> {
                List<Item> items = ItemQueries.getItemsByCategoryName(cat);

                TilePane itemsGrid = new TilePane();
                itemsGrid.setHgap(20);
                itemsGrid.setVgap(20);
                itemsGrid.setPrefColumns(3); // 3 columns
                itemsGrid.setPadding(new Insets(10));

                if (items.isEmpty()) {
                    Label noItemsLabel = new Label("No items found in this category.");
                    noItemsLabel.setStyle("-fx-font-size: 14; -fx-text-fill: gray;");
                    itemsGrid.getChildren().add(noItemsLabel);
                } else {
                    for (Item item : items) {
                        VBox itemCard = createItemCard(item.iname, item.iID);
                        itemsGrid.getChildren().add(itemCard);
                    }
                }

                VBox itemsContainer = (VBox) container.lookup("#itemsContainer");
                itemsContainer.getChildren().clear();
                itemsContainer.getChildren().add(itemsGrid);
            });
            catFlow.getChildren().add(catBtn);
        }

        Label info = new Label("Select a category above to browse products.\n"
                + "Then you can add them to your cart from the Items tab or category view.");
        info.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_COLOR + ";");

        VBox itemsContainer = new VBox(10);
        itemsContainer.setId("itemsContainer");  // Set an ID to reference this container
        itemsContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 8; -fx-padding: 10;");

        container.getChildren().addAll(title, catFlow, info, itemsContainer);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

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

        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search:");
        TextField searchField = buildTextField("Enter item name...");
        Button searchBtn = buildButton("Go");

        TilePane itemsGrid = new TilePane();
        itemsGrid.setHgap(20);
        itemsGrid.setVgap(20);
        itemsGrid.setPrefColumns(3); // 3 columns
        itemsGrid.setPadding(new Insets(10));

        List<Item> items = ItemQueries.getItems();
        for (Item item : items) {
            VBox itemCard = createItemCard(item.iname, item.iID);
            itemsGrid.getChildren().add(itemCard);
        }

        searchBtn.setOnAction(e -> {
            itemsGrid.getChildren().clear();

            List<Item> itemsByName = ItemQueries.getItemsByNameSubstring(searchField.getText());
            for (Item item : itemsByName) {
                VBox itemCard = createItemCard(item.iname, item.iID);
                itemsGrid.getChildren().add(itemCard);
            }

            String query = searchField.getText().trim();
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

        String imagePath = "images/" + itemName.replaceAll(" ", "_") + ".jpg"; // Assuming the image names match the item names
        ImageView imageView = new ImageView();
        File file = new File(imagePath);
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        } else {
            System.out.println("Image not found at: " + file.getAbsolutePath());
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        Button addBtn = buildButton("Add to Cart");
        Button reviewBtn = buildButton("Go to Reviews");

        card.setUserData(itemID);

        addBtn.setOnAction(e -> {
            CartedQueries.insertCartedItem(userID, itemID, 1);
            cartItems.clear();
            cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
        });

        card.getChildren().addAll(nameLabel, imageView, addBtn, reviewBtn);

        reviewBtn.setOnAction(e -> {
            selectedItemID = itemID;
            List<String> updatedReviews = ReviewQueries.getReviewsByItem(selectedItemID);
            reviewList.clear();
            reviewList.addAll(updatedReviews);
            switchToReviewsTab();
        });

        return card;
    }


    private void switchToReviewsTab() {
        if (tabPane != null) {
            tabPane.getSelectionModel().select(4); // Reviews tab is at index 4
            updateReviewsTab();
        }
    }

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
            PurchaseQueries.insertPurchase(userID, CartedQueries.getCustomerCartPrice(userID));
            PurchaseQueries.makePayment(userID);
            cartItems.clear();
            cartItems.addAll(CartedQueries.getCustomerCartItems(userID));
            purchaseList.clear();
            purchaseList.addAll(PurchaseQueries.getPurchases(userID));
        });

        container.getChildren().addAll(title, cartListView, purchaseBtn);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }

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

        ListView<Purchase> purchasesListView = new ListView<>(purchaseList);
        purchasesListView.setPrefHeight(200);
        purchasesListView.setCellFactory(lv -> new ListCell<Purchase>() {
            @Override
            protected void updateItem(Purchase item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Date: " + item.date + " | Price: $" + item.price);
                }
            }
        });

        purchaseList.clear();
        purchaseList.addAll(PurchaseQueries.getPurchases(userID));

        container.getChildren().addAll(title, purchasesListView);

        VBox wrapper = new VBox(container);
        wrapper.setPadding(new Insets(20));
        scroll.setContent(wrapper);
        return scroll;
    }


    private void updateReviewsTab() {
        if (selectedItemID != -1) {
            Item item = ItemQueries.getItemByID(selectedItemID);
            Label itemLabel = (Label) tabPane.lookup("#itemLabel");
            itemLabel.setText("Selected Item: " + item.iname);
        }
    }

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

        Label itemLabel = new Label("Selected Item: ");
        itemLabel.setId("itemLabel");  // Set an ID for easy access
        if (selectedItemID != -1) {
            Item item = ItemQueries.getItemByID(selectedItemID);
            itemLabel.setText("Selected Item: " + item.iname);
        }

        List<String> reviews = ReviewQueries.getReviewsByItem(selectedItemID);

        ListView<String> reviewsListView = new ListView<>();
        reviewsListView.setPrefHeight(200);

        reviewsListView.setItems(reviewList);

        Label reviewTitle = new Label("Write your review:");
        TextArea reviewArea = buildTextArea();
        reviewArea.setPromptText("Write your review here...");

        Button submitReviewButton = buildButton("Submit Review");
        submitReviewButton.setOnAction(e -> {
            String reviewText = reviewArea.getText().trim();
            if (!reviewText.isEmpty()) {
                ReviewQueries.insertReview(selectedItemID, reviewText);

                reviewArea.clear();

                List<String> updatedReviews = ReviewQueries.getReviewsByItem(selectedItemID);

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
