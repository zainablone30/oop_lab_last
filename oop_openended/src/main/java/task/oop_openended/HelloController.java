package task.oop_openended;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private Button ADD_button;

    @FXML
    private Button edit_button;

    @FXML
    private TextField product_id;

    @FXML
    private TextField product_image_link;

    @FXML
    private TextField product_name;

    @FXML
    private TextField product_price;

    @FXML
    private Button remove_button;

    @FXML
    private TableView<Product> product_table;
    @FXML
    private TableColumn<Product, Integer> id_table_view;

    @FXML
    private TableColumn<Product, String> link_column;

    @FXML
    private TableColumn<Product, String> name_column;

    @FXML
    private TableColumn<Product, Double> price_column;

    @FXML
    private TilePane tilePane;

    private ObservableList<Product> productList;

    public void initialize() {
        productList = FXCollections.observableArrayList();
        id_table_view.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        name_column.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        price_column.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        link_column.setCellValueFactory(cellData -> cellData.getValue().imageLinkProperty());
        loadProducts();
        displayProductsInTilePane();
        ADD_button.setOnAction(this::ADD_handler);
        edit_button.setOnAction(this::edit_handler);
        remove_button.setOnAction(this::remove_handler);
    }


    private void displayProductsInTilePane() {
        tilePane.getChildren().clear();

        // Retrieve products from the database and add to tilePane
        List<Product> products = getProductsFromDatabase();
        if (products.isEmpty()) {
            System.out.println("No products found in the database.");
        } else {
            for (Product product : products) {
                VBox productBox = new VBox();
                productBox.setAlignment(Pos.CENTER);
                productBox.setSpacing(20);

                Label nameLabel = new Label(product.getName());
                Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));

                ImageView imageView = new ImageView();
                if (product.getImageLink() != null) {
                    try {
                        imageView.setImage(new Image(product.getImageLink()));
                    } catch (Exception e) {
                        System.out.println("Error loading image: " + e.getMessage());
                    }
                }
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);

                productBox.getChildren().addAll(imageView, nameLabel, priceLabel);
                tilePane.getChildren().add(productBox);
            }
        }
    }

    private List<Product> getProductsFromDatabase() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, product_name, price, link FROM product";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String imagelink = rs.getString("link");

                products.add(new Product(id, name, price, imagelink));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    private void loadProducts() {
        String sql = "SELECT * FROM product";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                String imageLink = rs.getString("link");
                productList.add(new Product(id, name, price, imageLink));
            }
            product_table.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ADD_handler(ActionEvent event) {
        String name = product_name.getText();
        double price = Double.parseDouble(product_price.getText());
        String imageLink = product_image_link.getText();

        String sql = "INSERT INTO product (product_name, price, link) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, imageLink);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    productList.add(new Product(id, name, price, imageLink));
                }
            }
            product_table.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit_handler(ActionEvent event) {
        Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int id = selectedProduct.getId();
            String name = product_name.getText();
            double price = Double.parseDouble(product_price.getText());
            String imageLink = product_image_link.getText();

            String sql = "UPDATE product SET product_name = ?, price = ?, link = ? WHERE id = ?";

            try (Connection conn = databaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setString(3, imageLink);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();

                selectedProduct.setName(name);
                selectedProduct.setPrice(price);
                selectedProduct.setImageLink(imageLink);
                product_table.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void remove_handler(ActionEvent event) {
        Product selectedProduct = product_table.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            String sql = "DELETE FROM product WHERE id = ?";

            try (Connection conn = databaseConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selectedProduct.getId());
                pstmt.executeUpdate();
                productList.remove(selectedProduct);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
