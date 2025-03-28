import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bouncycastle.jcajce.provider.digest.Keccak;

public class signUpController {
    private Stage stage;
    private Scene scene;

    // Sign Up form alanları
    @FXML
    private TextField tcSignUp;  // fx:id="tcSignUp"
    @FXML
    private TextField usernameSignUp; // fx:id="usernameSignUp"
    @FXML
    private PasswordField passwordSignUp; // fx:id="passwordSignUp"
    @FXML
    private TextField fnameSignUp; // fx:id="fnameSignUp"
    @FXML
    private TextField lnameSignUp; // fx:id="lnameSignUp"

    @FXML
    private Text signUpError; // fx:id="signUpError"

    /**
     * "Sign Up" butonuna basıldığında çağrılır.
     * Boşluk kontrolü + veritabanına ekleme + Login ekranına geçiş
     */
    @FXML
    public void handleSignUp(MouseEvent event) throws IOException {
        String username = usernameSignUp.getText().trim();
        String password = passwordSignUp.getText().trim();
        String fname = fnameSignUp.getText().trim();
        String lname = lnameSignUp.getText().trim();

        if (username == null || username.isEmpty()) {
            signUpError.setText("Username cannot be empty!");
            return;
        }
        if (password == null || password.isEmpty()) {
            signUpError.setText("Password cannot be empty!");
            return;
        }
        if (fname == null || fname.isEmpty()) {
            signUpError.setText("First Name cannot be empty!");
            return;
        }
        if (lname == null || lname.isEmpty()) {
            signUpError.setText("Last Name cannot be empty!");
            return;
        }

        String insertQuery = "INSERT INTO users (user_id, username, user_password, full_name, user_role) VALUES (?, ?, ?, ROW(?, ?), 'member')";

        // Hashing password to keccak256 hex string
        String hashedPassword = hashPassword(password);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, null); // user_id (null yapılarak trigger tetiklenir)
            preparedStatement.setString(2, username);  // username
            preparedStatement.setString(3, hashedPassword); // user_password
            // full_name fields (fname, lname)
            preparedStatement.setString(4, fname);  // fname
            preparedStatement.setString(5, lname);  // lname

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                signUpError.setText("Username already exists!");
            } else {
                signUpError.setText("Error during sign up. Please try again.");
                e.printStackTrace();
            }
            return;
        }

        signUpError.setText("");

        // Kullanıcı kaydolduktan sonra Login ekranına geçer
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private String hashPassword(String password) {
        Keccak.Digest256 keccak256 = new Keccak.Digest256();
        byte[] hashBytes = keccak256.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public void switchToLoginScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHelloScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/hello-view.fxml"));
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