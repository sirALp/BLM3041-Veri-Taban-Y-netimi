import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceController {

    private Stage stage;
    private Scene scene;

    private List<MarketItemModel> itemList = new ArrayList<>();

    @FXML
    private FlowPane marketItemsContainer;

    @FXML
    public void initialize() {
        itemList = fetchMarketItemsFromDB();
        refreshItems();
    }

    private List<MarketItemModel> fetchMarketItemsFromDB() {
        List<MarketItemModel> items = new ArrayList<>();
        String query = "SELECT item_id, seller_id, " +
                "(product_info).product_name AS name, " +
                "(product_info).price_per_kg AS price, " +
                "(product_info).quantity AS quantity, " +
                "is_active " +
                "FROM market_items";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String itemId = resultSet.getString("item_id");
                String itemName = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String sellerId = resultSet.getString("seller_id");
                Boolean isActive = resultSet.getBoolean("is_active");

                items.add(new MarketItemModel(itemId,itemName, price, quantity, sellerId, isActive));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void refreshItems() {
        marketItemsContainer.getChildren().clear();

        for (MarketItemModel item : itemList) {
            if (item.isActive()) {
                try {
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml_files/MarketItem.fxml"));
                    Node cardNode = loader.load();
                    
                    marketItemController cardController = loader.getController();
                    cardController.setData(item.getItemId(), item.getItemName(), item.getPrice(), item.getQuantity()); // itemId ekleniyor
                    
                    marketItemsContainer.getChildren().add(cardNode);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void switchToSellScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/sellitem.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMarketplaceScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Marketplace.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTicketScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Ticket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPurchases(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/purchases.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRentalScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Rental.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}