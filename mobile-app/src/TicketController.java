import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

public class TicketController {


    private Stage stage;
    private Scene scene;

    @FXML
    private ChoiceBox<Integer> choiceQntTicketBox;

    @FXML
    private TextArea ticketMsgField;

    @FXML
    private Text succesTicketSend;

    @FXML
    private Pane confirmationPane;

    private String tempEquipmentName;
    private Integer tempQuantity;

    private final Integer[] choiceQntTicket = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    @FXML
    public void initialize() {
        choiceQntTicketBox.getItems().addAll(choiceQntTicket);
    }

    @FXML
    public void showConfirmationPopup(MouseEvent event) {
        tempEquipmentName = ticketMsgField.getText();
        tempQuantity = choiceQntTicketBox.getValue();

        if (tempEquipmentName == null || tempEquipmentName.trim().isEmpty()) {
            succesTicketSend.setText("Equipment name cannot be empty!");
            return;
        }
        if (tempQuantity == null) {
            succesTicketSend.setText("Quantity is not selected!");
            return;
        }

        confirmationPane.setVisible(true);
    }

    @FXML
    public void handleYes(MouseEvent event) {
        String role = null;
        try (Connection connection = DBConnection.getConnection()) {
            // Kullanıcının rolünü almak için SELECT sorgusu
            String roleQuery = "SELECT user_role FROM users WHERE user_id = ?";
            PreparedStatement roleStatement = connection.prepareStatement(roleQuery);

            // SessionManager'dan kullanıcı ID'sini alır
            String currentUserId = SessionManager.getInstance().getCurrentUser().getUserId();
            roleStatement.setString(1, currentUserId);

            var resultSet = roleStatement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("user_role");
            }

            // Kullanıcı rolü kontrolü
            if ("member".equalsIgnoreCase(role)) {
                succesTicketSend.setText("You don't have any garden!");
                confirmationPane.setVisible(false);
                ticketMsgField.clear();
                choiceQntTicketBox.setValue(null);
                return;
            }

            if ("tenant".equalsIgnoreCase(role)) {
                // Kullanıcı tenant ise ticket oluştur
                String insertQuery = "INSERT INTO tickets (ticket_id, user_id, equipment, ticket_status) " +
                        "VALUES (NULL, ?, ROW(?, ?)::equipment_type, 'pending'::ticket_status_type)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

                insertStatement.setString(1, currentUserId);
                insertStatement.setString(2, tempEquipmentName);
                insertStatement.setInt(3, tempQuantity);

                insertStatement.executeUpdate();
                succesTicketSend.setText("Ticket sent successfully!");
            } else {
                succesTicketSend.setText("Invalid user role!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            succesTicketSend.setText("Error checking user role or sending ticket!");
        }

        confirmationPane.setVisible(false);
        ticketMsgField.clear();
        choiceQntTicketBox.setValue(null);
    }

    @FXML
    public void handleNo(MouseEvent event) {
        confirmationPane.setVisible(false);
        succesTicketSend.setText("Ticket sending cancelled!");
    }

    public void switchToShowTickets(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/showTickets.fxml"));
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