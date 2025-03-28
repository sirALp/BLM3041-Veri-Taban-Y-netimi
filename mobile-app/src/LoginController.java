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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bouncycastle.jcajce.provider.digest.Keccak;

public class LoginController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField usernameLogin;  // FXML'de fx:id="usernameLogin"
    @FXML
    private PasswordField passwordLogin; // fx:id="passwordLogin"
    @FXML
    private Text loginError; // fx:id="loginError"

    /**
     * "Login" butonu tıklanınca çağrılır.
     * Eğer username/password boşsa ekranda hata yazar, değilse veritabanını kontrol et ve home.fxml'e geç.
     */
    @FXML
    public void handleLogin(MouseEvent event) throws IOException {
        String username = usernameLogin.getText();
        String password = passwordLogin.getText();
    
        // Empty field checks
        if (username == null || username.trim().isEmpty()) {
            loginError.setText("Username cannot be empty!");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            loginError.setText("Password cannot be empty!");
            return;
        }

        // Hash the password
        String hashedPassword = hashPassword(password);
    
        // Use the database function to check login
        String query = "SELECT user_id, username, full_name, user_role FROM checkUserPassword(?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                // Login successful, get user data
                String userId = resultSet.getString("user_id");
                String uname = resultSet.getString("username");
                String fullName = resultSet.getString("full_name");
                String userRole = resultSet.getString("user_role");
                
                fullName = fullName.substring(1, fullName.length() - 1);

                // Parse full name
                String fname = "";
                String lname = "";
                if (fullName != null) {
                    String[] names = fullName.split(",");
                    if (names.length > 0) {
                        fname = names[0];
                        if (names.length > 1) {
                            lname = names[1];
                        }
                    }
                }
                
                System.out.println("User fname and lname: " + fname + " " + lname);

                // Create user model object
                UserModel user = new UserModel(userId, uname, null, fname, lname, userRole);
    
                // Set current user in session manager
                SessionManager.getInstance().setCurrentUser(user);
    
                System.out.println("Logged in, username: " + uname);
    
                // Proceed to Home screen
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Home.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            } else {
                // Login failed
                loginError.setText("Invalid username or password!");
            }
    
        } catch (SQLException e) {
            loginError.setText("Error during login. Please try again.");
            e.printStackTrace();
        }
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

    public void switchToSignUpScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/signUp.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoginScene(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml_files/Login.fxml"));
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