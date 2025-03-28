import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaymentGardenController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField paymentCrNo;
    @FXML
    private TextField paymentExpMonth;
    @FXML
    private TextField paymentExpYear;
    @FXML
    private PasswordField paymentCvv;
    @FXML
    private Text duePaymentErrorText;

    private String gardenId; // Seçilen Garden ID'si

    /**
     * Garden ID'sini set eder.
     */
    public void setGardenId(String gardenId) {
        this.gardenId = gardenId;
        System.out.println("Garden ID received in PaymentGardenController: " + gardenId);
    }
    @FXML
    private void initialize() {
        System.out.println("Garden ID during initialization: " + gardenId);
    }

    @FXML
    private void handlePayButtonAction() throws IOException {
        String crNo = paymentCrNo.getText();
        String expMonth = paymentExpMonth.getText();
        String expYear = paymentExpYear.getText();
        String cvv = paymentCvv.getText();

        if (crNo.isEmpty() || expMonth.isEmpty() || expYear.isEmpty() || cvv.isEmpty()) {
            duePaymentErrorText.setText("Fill all required fields.");
            return;
        }

        // Ödeme işlemi gerçekleştirir
        boolean paymentSuccess = processPayment(crNo, expMonth, expYear, cvv);

        if (paymentSuccess) {
            // Veritabanında garden'ın status'unu günceller ve rental kaydı oluştur
            boolean updateSuccess = updateGardenStatusAndCreateRental();

            if (updateSuccess) {
                duePaymentErrorText.setText("Payment successful. Garden Rented Successfully.");
                switchToRentalScene();
            } else {
                duePaymentErrorText.setText("Error occurred!");
            }

        } else {
            duePaymentErrorText.setText("Payment failed! Try again.");
        }
    }

    /**
     * Ödeme işlemini simüle eder. Gerçek Hayat uygulmasında API ile kontrolü sağlamalıyız.
     *
     */
    private boolean processPayment(String crNo, String expMonth, String expYear, String cvv) {
        // Basit bir kontrol: tüm alanların dolu olması
        return !crNo.isEmpty() && !expMonth.isEmpty() && !expYear.isEmpty() && !cvv.isEmpty();
    }

    /**
     * Garden'ın status'unu 'occupied' yapar ve rental kaydı oluşturur.
     */
    private boolean updateGardenStatusAndCreateRental() {
        System.out.println("Garden ID just before SQL execution: " + gardenId);
        if (gardenId == null || gardenId.isEmpty()) {
            duePaymentErrorText.setText("Garden ID geçersiz!");
            return false;
        }
        String insertRentalQuery = "INSERT INTO rentals (user_id, garden_id, rental_date) VALUES (?, ?, ROW(?, ?)::rental_date_type)";

        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            duePaymentErrorText.setText("Kullanıcı oturumu bulunamadı.");
            return false;
        }
        String userId = currentUser.getUserId();

        System.out.println("SQL Query: " + insertRentalQuery);
        System.out.println("User ID: " + userId);
        System.out.println("Garden ID: " + gardenId);
        System.out.println("Start Date: " + LocalDate.now());
        System.out.println("End Date: " + LocalDate.now().plusMonths(6));

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false); // Transaction başlatılır

            try (PreparedStatement insertRentalStmt = connection.prepareStatement(insertRentalQuery)) {

                // Rental kaydı ekleme
                insertRentalStmt.setString(1, userId); // user_id
                insertRentalStmt.setString(2, gardenId); // garden_id
                insertRentalStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // start_date
                insertRentalStmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusMonths(6)));
                insertRentalStmt.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void switchToRentalScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Rental.fxml"));
        Stage stage = (Stage) paymentCrNo.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
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
}