import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class purchasesController {

    @FXML
    private TableView<MarketSaleModel> Ptable;

    @FXML
    private TableColumn<MarketSaleModel, java.sql.Date> Pdate;

    @FXML
    private TableColumn<MarketSaleModel, String> Pitem;

    @FXML
    private TableColumn<MarketSaleModel, Integer> Pquantity;

    @FXML
    private TableColumn<MarketSaleModel, Integer> Pprice;

    @FXML
    private Text errorText;

    private Stage stage;
    private Scene scene;


    @FXML
    public void initialize() {
        Pdate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        Pitem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        Pquantity.setCellValueFactory(new PropertyValueFactory<>("quantitySold"));
        Pprice.setCellValueFactory(new PropertyValueFactory<>("pricePerKg"));

        loadUserPurchases();
    }

    /**
     * Belirli bir buyer_id'ye ait satın alma kayıtlarını çeker ve TableView'a ekler.
     */
    private void loadUserPurchases() {
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("No user session found.");
            if (errorText != null) {
                errorText.setText("Kullanıcı oturumu bulunamadı.");
            }
            return;
        }
        String buyerId = currentUser.getUserId();

        // Satın alma verilerini çekmek için SQL sorgusu (VİEW KULLANILIYOR)
        String query = "SELECT * FROM user_sales_view WHERE buyer_id = ?";

        ObservableList<MarketSaleModel> purchases = FXCollections.observableArrayList();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, buyerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String saleId = resultSet.getString("sale_id");
                String itemId = resultSet.getString("item_id");
                String fetchedBuyerId = resultSet.getString("buyer_id");
                int quantitySold = resultSet.getInt("quantity_sold");
                java.sql.Date transactionDate = resultSet.getDate("transaction_date");
                String itemName = resultSet.getString("item_name");
                int pricePerKg = resultSet.getInt("price_per_kg");
                String buyerUsername = resultSet.getString("buyer_username");

                MarketSaleModel purchase = new MarketSaleModel(
                        saleId,
                        itemId,
                        fetchedBuyerId,
                        quantitySold,
                        transactionDate,
                        itemName,
                        pricePerKg,
                        buyerUsername
                );

                purchases.add(purchase);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (errorText != null) {
                errorText.setText("Veritabanı hatası: " + e.getMessage());
            }
        }

        Ptable.setItems(purchases);
    }


    public void switchToShowRecentRentals(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/showRentals.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTicketScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Ticket.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMarketplaceScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Marketplace.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRentalScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Rental.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}