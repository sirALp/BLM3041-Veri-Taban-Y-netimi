import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.IOException;

public class GardenItemController {

    private Stage stage;
    private Scene scene;


    @FXML
    private Text gardenLocationValue;
    @FXML
    private Text gardenArceText;
    @FXML
    private Text gardenPriceText;
    @FXML
    private Button RentButton;

    private String gardenId;

    public void setData(String gardenId, String location, int acres, int price, String status) {
        this.gardenId = gardenId;
        gardenLocationValue.setText(location);
        gardenArceText.setText(String.valueOf(acres));
        gardenPriceText.setText(String.valueOf(price));

        RentButton.setOnAction(e -> {
            try {
                openPaymentScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Ödeme ekranını açar ve seçilen garden ID'sini ödeme kontrolcüsüne iletir.
     */
    private void openPaymentScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml_files/paymentGarden.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load());

        // PaymentGardenController'a gardenId'yi aktar
        PaymentGardenController paymentController = loader.getController();
        paymentController.setGardenId(this.gardenId);


        System.out.println("Garden ID passed to PaymentGardenController: " + this.gardenId);

    }

    public void switchToPaymentScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/paymentGarden.fxml"));
        Node root = fxmlLoader.load();

        // Kontrolcüye erişim ve gardenId'nin aktarılması
        PaymentGardenController paymentController = fxmlLoader.getController();
        paymentController.setGardenId(this.gardenId);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.show();

        // Kontrol Logu
        System.out.println("Garden ID passed to PaymentGardenController: " + this.gardenId);
    }

    public void switchToRentalScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Rental.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}