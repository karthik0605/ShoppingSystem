//import javax.swing.*;
//import java.awt.*;
//
//public class ShoppingUI extends JFrame {
//
//    // UI components
//    private JButton viewCartButton;
//    private JTextField searchField;
//    private JComboBox<String> categoryComboBox;
//    private JList<String> productList;
//
//    public ShoppingUI() {
//        setTitle("Online Shopping System");
//        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Top panel that will contain the search bar, category drop-down, and view cart button
//        JPanel topPanel = new JPanel(new BorderLayout());
//
//        // Left panel for the search bar and category drop-down
//        JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        searchField = new JTextField(20);
//        searchField.setToolTipText("Search for products...");
//
//        // Create combo box for categories; initially, we add a placeholder.
//        categoryComboBox = new JComboBox<>();
//        categoryComboBox.addItem("Select Category");
//        // Later you can populate this combo box with categories from your database.
//
//        leftTopPanel.add(searchField);
//        leftTopPanel.add(categoryComboBox);
//
//        // Right panel for the view cart button
//        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        viewCartButton = new JButton("View Cart");
//        rightTopPanel.add(viewCartButton);
//
//        // Add both panels to the top panel
//        topPanel.add(leftTopPanel, BorderLayout.WEST);
//        topPanel.add(rightTopPanel, BorderLayout.EAST);
//
//        // Center panel for the product list, placed inside a scroll pane
//        // Initially, the list model is empty.
//        productList = new JList<>(new DefaultListModel<>());
//        JScrollPane scrollPane = new JScrollPane(productList);
//
//        // Add components to the frame
//        add(topPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//    }
//
//    public static void main(String[] args) {
//        // Schedule the GUI creation on the Event Dispatch Thread
//        SwingUtilities.invokeLater(() -> new ShoppingUI().setVisible(true));
//    }
//}
