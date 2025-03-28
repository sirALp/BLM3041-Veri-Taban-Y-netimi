import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class sellitemController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextArea itemNameTxtField;

    @FXML
    private TextArea itemPriceTxtField;

    @FXML
    private ChoiceBox<Integer> choiceQntTicketBox;

    @FXML
    private Button sellButtonMarket;

    @FXML
    private Pane confirmationPane;

    @FXML
    private Text sellitemInfoTxt;

    private String tempItemName;
    private Integer tempQuantity;
    private String tempPrice;

    @FXML
    public void initialize() {
        choiceQntTicketBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @FXML
    public void showConfirmationPopup(MouseEvent event) {
        tempItemName = itemNameTxtField.getText();
        tempQuantity = choiceQntTicketBox.getValue();
        tempPrice = itemPriceTxtField.getText();

        if (tempItemName == null || tempItemName.isEmpty()) {
            sellitemInfoTxt.setText("Item name cannot be empty!");
            return;
        }

        if (tempQuantity == null) {
            sellitemInfoTxt.setText("Quantity is not selected!");
            return;
        }

        try {
            int priceAsInt = Integer.parseInt(tempPrice);
            if (priceAsInt < 1) {
                sellitemInfoTxt.setText("Price must be a positive number!");
                return;
            }
        } catch (NumberFormatException e) {
            sellitemInfoTxt.setText("Price must be a number!");
            return;
        }

        // Kullanıcının en az bir garden'a sahip olup olmadığını kontrol eder
        if (!hasGarden()) {
            sellitemInfoTxt.setText("You must own at least one garden to sell items!");
            return;
        }

        confirmationPane.setVisible(true);
    }

    private boolean hasGarden() {
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            sellitemInfoTxt.setText("User not logged in!");
            return false;
        }

        String userId = currentUser.getUserId();
        String query = "SELECT COUNT(*) AS garden_count FROM gardens WHERE garden_id IN " +
                "(SELECT garden_id FROM rentals WHERE user_id = ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int gardenCount = resultSet.getInt("garden_count");
                return gardenCount > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sellitemInfoTxt.setText("Error checking garden ownership!");
        }
        return false;
    }

    @FXML
    public void handleYes(MouseEvent event) {
        int price = Integer.parseInt(tempPrice);

        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            sellitemInfoTxt.setText("User not logged in!");
            return;
        }

        String sellerId = currentUser.getUserId();

        try (Connection connection = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO market_items (seller_id, product_info) VALUES (?, ROW(?, ?, ?)::product_info_type)";

            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, sellerId);
                statement.setString(2, tempItemName.toString().trim());
                statement.setInt(3, price);
                statement.setInt(4, tempQuantity);
                statement.executeUpdate();

                System.out.println("Item successfully added to marketplace!");

                switchToMarketplaceScene(event);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            sellitemInfoTxt.setText("Error adding item to database!");
        }

        confirmationPane.setVisible(false);
        itemNameTxtField.clear();
        choiceQntTicketBox.setValue(null);
        itemPriceTxtField.clear();
        sellitemInfoTxt.setText(" ");
    }

    @FXML
    public void handleNo(MouseEvent event) {
        confirmationPane.setVisible(false);
    }

    public void switchToTicketScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Ticket.fxml"));
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

    public void switchToHomeScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Home.fxml"));
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
}