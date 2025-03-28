import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class marketItemController {

    @FXML
    private Text itemNameText;

    @FXML
    private ChoiceBox<Integer> kgChoiceBox;

    @FXML
    private Text pricePerKgText;

    @FXML
    private Button BuyButton;


    private Stage stage;

    private Scene scene;

    private int selectedKg;
    private String itemId;



    public void setData(String itemId, String itemName, int pricePerKg, int quantity) {
        this.itemId = itemId;

        itemNameText.setText(itemName);
        pricePerKgText.setText(String.valueOf(pricePerKg));

        kgChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        kgChoiceBox.setValue(1);

        BuyButton.setOnAction(e -> {
            selectedKg = kgChoiceBox.getValue();
            System.out.println("Buying " + selectedKg + " kg of " + itemName + " @ " + pricePerKg + " per kg");

            try {
                openPaymentMarketItem(itemId);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    public void openPaymentMarketItem(String itemId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/paymentMarketItem.fxml"));
        Parent root = loader.load();

        paymentMarketItemController paymentController = loader.getController();
        paymentController.setSelectedKg(this.selectedKg);
        paymentController.setItemId(itemId); // itemId'yi iletin

        Stage currentStage = (Stage) BuyButton.getScene().getWindow();
        Scene paymentScene = new Scene(root);
        currentStage.setScene(paymentScene);
        currentStage.show();

        System.out.println("Selected KG passed to PaymentMarketItemController: " + this.selectedKg);
        System.out.println("Item ID passed to PaymentMarketItemController: " + itemId);
    }

    public void switchToPaymentScene(MouseEvent event) throws IOException {
        if (stage == null) {

            try {
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            } catch (Exception e) {
                System.err.println("Stage could not be initialized: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        if (stage != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/paymentMarketItem.fxml"));
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else {

        }
    }
}