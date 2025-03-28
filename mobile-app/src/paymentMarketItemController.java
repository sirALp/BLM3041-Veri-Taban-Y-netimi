import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class paymentMarketItemController {

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

    private String itemId;
    private int selectedKg;

    public void setSelectedKg(int selectedKg){
        this.selectedKg = selectedKg;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
        System.out.println("Item ID set to: " + itemId);
    }

    @FXML
    private void handlePayButtonAction() {
        String crNo = paymentCrNo.getText();
        String expMonth = paymentExpMonth.getText();
        String expYear = paymentExpYear.getText();
        String cvv = paymentCvv.getText();

        if (crNo.isEmpty() || expMonth.isEmpty() || expYear.isEmpty() || cvv.isEmpty()) {
            duePaymentErrorText.setText("Tüm alanları doldurunuz.");
            return;
        }



        boolean paymentSuccess = processPayment(crNo, expMonth, expYear, cvv);

        if (paymentSuccess) {
            // Veritabanında sales kaydı oluşturur
            boolean insertSuccess = CreateSales();

            if (insertSuccess) {

                duePaymentErrorText.setText("Ödeme başarılı! Satış kaydedildi.");

                try {
                    switchToMarketplaceScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                duePaymentErrorText.setText("Bir hata oluştu. Lütfen tekrar deneyiniz.");
            }

        } else {
            duePaymentErrorText.setText("Ödeme başarısız. Lütfen bilgilerinizi kontrol ediniz.");
        }
    }

    private boolean CreateSales() {
        System.out.println("Item ID just before SQL execution: " + itemId);

        String selectQuantityQuery = "SELECT (product_info).quantity FROM market_items WHERE item_id = ?";
        String insertSalesQuery = "INSERT INTO sales (item_id, buyer_id, quantity_sold, transaction_date) VALUES (?, ?, ?, ?)";
        String updateQuantityQuery = "UPDATE market_items SET product_info = ROW((product_info).product_name, (product_info).price_per_kg, (product_info).quantity - ?) WHERE item_id = ?";

        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            duePaymentErrorText.setText("Kullanıcı oturumu bulunamadı.");
            return false;
        }
        String buyer_id = currentUser.getUserId();

        System.out.println("SQL Queries prepared for execution.");
        System.out.println("Buyer ID: " + buyer_id);
        System.out.println("Item ID: " + itemId);
        System.out.println("Quantity: " + selectedKg);
        System.out.println("Transaction Date: " + LocalDate.now());

        try (Connection connection = DBConnection.getConnection()) {
            try {
                // Transaction başlatır
                connection.setAutoCommit(false);

                // 1. Mevcut quantity kontrolü
                PreparedStatement selectStmt = connection.prepareStatement(selectQuantityQuery);
                selectStmt.setString(1, itemId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int currentQuantity = rs.getInt(1);
                    System.out.println("Current Quantity for item_id " + itemId + ": " + currentQuantity);

                    if (selectedKg > currentQuantity) {
                        duePaymentErrorText.setText("Yeterli miktar yok. Mevcut miktar: " + currentQuantity);
                        connection.rollback();
                        return false;
                    }

                    // 2. Sales kaydı ekleme
                    PreparedStatement insertSalesStmt = connection.prepareStatement(insertSalesQuery);
                    insertSalesStmt.setString(1, itemId); // item_id
                    insertSalesStmt.setString(2, buyer_id); // buyer_id
                    insertSalesStmt.setInt(3, selectedKg); // quantity_sold
                    insertSalesStmt.setDate(4, java.sql.Date.valueOf(LocalDate.now())); // transaction_date
                    insertSalesStmt.executeUpdate();
                    System.out.println("Sales record inserted successfully.");

                    // 3. Market item quantity güncelleme
                    PreparedStatement updateQuantityStmt = connection.prepareStatement(updateQuantityQuery);
                    updateQuantityStmt.setInt(1, selectedKg); // quantity_sold
                    updateQuantityStmt.setString(2, itemId); // item_id
                    updateQuantityStmt.executeUpdate();
                    System.out.println("Market item quantity updated successfully.");

                    connection.commit();
                    duePaymentErrorText.setText("Ödeme başarılı! Satış kaydedildi.");
                    return true;
                } else {
                    duePaymentErrorText.setText("Seçilen öğe bulunamadı.");
                    connection.rollback();
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
                duePaymentErrorText.setText("Bir hata oluştu. Lütfen tekrar deneyiniz.");
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            duePaymentErrorText.setText("Bir hata oluştu. Lütfen tekrar deneyiniz.");
            return false;
        }
    }

    private boolean processPayment(String crNo, String expMonth, String expYear, String cvv) {
        return !crNo.isEmpty() && !expMonth.isEmpty() && !expYear.isEmpty() && !cvv.isEmpty();
    }

    @FXML
    private void switchToMarketplaceScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Marketplace.fxml"));
        Parent root = fxmlLoader.load();

        Stage currentStage = (Stage) paymentCrNo.getScene().getWindow();
        Scene marketplaceScene = new Scene(root);
        currentStage.setScene(marketplaceScene);
        currentStage.show();
    }

}