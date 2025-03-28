import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class showRentalsController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TableView<RentalModel> SRtable;

    @FXML
    private TableColumn<RentalModel, String> SRgarden;

    @FXML
    private TableColumn<RentalModel, String> SRstartdate;

    @FXML
    private TableColumn<RentalModel, String> SRenddate;

    /**
     * Initialize metodunda tablo sütunlarını ayarlıyoruz ve verileri çekiyoruz.
     */
    @FXML
    private void initialize() {
        // Sütunların RentalModel'deki property'lere bağlanması
        SRgarden.setCellValueFactory(new PropertyValueFactory<>("garden"));
        SRstartdate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        SRenddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        SRtable.setItems(getRentals());
    }

    /**
     * Veritabanından kiralama verilerini çeker.
     * @return Kullanıcının kiralama verilerini içeren ObservableList
     */
    private ObservableList<RentalModel> getRentals() {
        ObservableList<RentalModel> rentals = FXCollections.observableArrayList();

        // Oturum açmış kullanıcının ID'sini alır
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("No user session found.");
            return rentals;
        }
        String userId = currentUser.getUserId();

        // Kiralama verilerini çeken SQL Sorgusu
        String query = "SELECT g.locations AS garden_name, " +
                "(r.rental_date).start_date AS start_date, " +
                "(r.rental_date).end_date AS end_date " +
                "FROM rentals r " +
                "JOIN gardens g ON r.garden_id = g.garden_id " +
                "WHERE r.user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String gardenName = resultSet.getString("garden_name");
                String startDate = resultSet.getDate("start_date").toString();
                String endDate = resultSet.getDate("end_date").toString();

                rentals.add(new RentalModel(gardenName, startDate, endDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }


    public void switchToShowTickets(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/showTickets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTicketScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Ticket.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMarketplaceScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Marketplace.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRentalScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Rental.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomeScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}