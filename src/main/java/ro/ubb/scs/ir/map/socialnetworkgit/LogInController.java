package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.io.IOException;

public class LogInController {

    Utilizator utilizator;

    private UserController controller;
    private String user_password;

    @FXML
    private Button LogInButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Label label;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink hyperlink;

    private Stage stage;

    UtilizatorService service;

    @FXML
    private void initialize()
    {
        Image image = new Image("eye_resize.png");

        ImageView imageView = new ImageView(image);

        label.setGraphic(imageView);

        String css = getClass().getResource("views/styles.css").toExternalForm();
        toggleButton.getStylesheets().add(css);

        hyperlink.setOnAction(event -> {
            try {
                handleChangePassword();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        passwordField.textProperty().addListener((observableValue, s, t1) -> passwordTextField.setText(t1));

        toggleButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (toggleButton.isSelected()) {
                passwordTextField.setManaged(true);
                passwordTextField.setVisible(true);
                passwordField.setManaged(false);
                passwordField.setVisible(false);
            } else {
                passwordTextField.setManaged(false);
                passwordTextField.setVisible(false);
                passwordField.setManaged(true);
                passwordField.setVisible(true);
            }
        });
    }

    public void setService(UtilizatorService service, Utilizator utilizator, Stage stage, UserController controller)
    {
        this.service = service;
        this.utilizator = utilizator;
        this.controller = controller;
        this.stage = stage;
    }

    public void handleLogIn()
    {
        String password = passwordTextField.getText();

        if(service.checkPassword(utilizator, password) == true) {
            controller.showFriendsController(utilizator);
            stage.close();
        }
        else {
            AlertController.showErrorMessage(null, "Parola incorecta!");
            passwordTextField.setStyle("-fx-border-color: red;");
            passwordField.setStyle("-fx-border-color: red;");
        }
    }

    public void handleChangePassword() throws IOException {
        controller.showChangePasswordController(utilizator);
    }



}
