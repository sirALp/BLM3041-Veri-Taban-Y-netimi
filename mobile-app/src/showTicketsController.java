import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class showTicketsController {

    @FXML
    private TableView<ticketModel> STtable;

    @FXML
    private TableColumn<ticketModel, String> STdate;

    @FXML
    private TableColumn<ticketModel, String> STequipment;

    @FXML
    private TableColumn<ticketModel, Integer> STquantity;

    @FXML
    private TableColumn<ticketModel, String> STstatus;

    private Stage stage;
    private Scene scene;


    @FXML
    public void initialize() {
        STequipment.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        STquantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        STstatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        STtable.getItems().setAll(fetchTickets());
    }

    private List<ticketModel> fetchTickets() {
        List<ticketModel> tickets = new ArrayList<>();
        String query = "SELECT * FROM get_tickets_by_user(?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            UserModel currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("User is not logged in.");
            }

            preparedStatement.setString(1, currentUser.getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String ticketId = resultSet.getString("ticket_id");
                String userId = resultSet.getString("user_id");
                String equipmentName = resultSet.getString("equipment_name");
                int quantity = resultSet.getInt("quantity");
                String ticketStatus = resultSet.getString("ticket_status");

                tickets.add(new ticketModel(ticketId, equipmentName, quantity, ticketStatus));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
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