import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RentalController implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private FlowPane gardensContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<GardenModel> gardens = fetchAvailableGardensFromDB();
        loadGardenCards(gardens);
    }

    /**
     * Veritabanından statusu 'vacant' olan gardenları çeker.
     */
    private List<GardenModel> fetchAvailableGardensFromDB() {
        List<GardenModel> gardens = new ArrayList<>();
        String query = "SELECT garden_id, locations, garden_status, acres, monthly_price FROM gardens WHERE garden_status = 'vacant'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                gardens.add(new GardenModel(
                        resultSet.getString("garden_id"),
                        resultSet.getString("locations"),
                        resultSet.getString("garden_status"),
                        resultSet.getInt("acres"),
                        resultSet.getInt("monthly_price")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gardens;
    }


    private void loadGardenCards(List<GardenModel> gardens) {
        gardensContainer.getChildren().clear();

        for (GardenModel garden : gardens) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("/fxml_files/GardenItem.fxml")
                );

                Node cardNode = loader.load();

                GardenItemController cardController = loader.getController();
                cardController.setData(
                        garden.getGardenId(),
                        garden.getLocation(),
                        garden.getAcres(),
                        garden.getPrice(),
                        garden.getStatus()
                );

                gardensContainer.getChildren().add(cardNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void switchToShowRecentRentals(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/showRentals.fxml"));
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

    public void switchToHomeLoginScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Login.fxml"));
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