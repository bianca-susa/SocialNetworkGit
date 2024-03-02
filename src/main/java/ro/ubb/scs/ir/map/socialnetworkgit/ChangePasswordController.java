package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

public class ChangePasswordController {

    private UtilizatorService service;

    private Utilizator utilizator;

    private Stage stage;

    @FXML
    private TextField oldPasswordTextField;

    @FXML
    private PasswordField oldPasswordPasswordField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private PasswordField newPasswordPasswordField;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private PasswordField confirmPasswordPasswordField;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Label label;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize()
    {
        toggleButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (toggleButton.isSelected()) {
                oldPasswordTextField.setManaged(true);
                oldPasswordTextField.setVisible(true);

                newPasswordTextField.setManaged(true);
                newPasswordTextField.setVisible(true);

                confirmPasswordTextField.setManaged(true);
                confirmPasswordTextField.setVisible(true);

                oldPasswordPasswordField.setManaged(false);
                oldPasswordPasswordField.setVisible(false);

                newPasswordPasswordField.setManaged(false);
                newPasswordPasswordField.setVisible(false);

                confirmPasswordPasswordField.setManaged(false);
                confirmPasswordPasswordField.setVisible(false);

            } else {
                oldPasswordTextField.setManaged(false);
                oldPasswordTextField.setVisible(false);

                newPasswordTextField.setManaged(false);
                newPasswordTextField.setVisible(false);

                confirmPasswordTextField.setManaged(false);
                confirmPasswordTextField.setVisible(false);

                oldPasswordPasswordField.setManaged(true);
                oldPasswordPasswordField.setVisible(true);

                newPasswordPasswordField.setManaged(true);
                newPasswordPasswordField.setVisible(true);

                confirmPasswordPasswordField.setManaged(true);
                confirmPasswordPasswordField.setVisible(true);
            }
        });

        Image image = new Image("eye_resize.png");

        ImageView imageView = new ImageView(image);

        label.setGraphic(imageView);

        String css = getClass().getResource("views/styles.css").toExternalForm();
        toggleButton.getStylesheets().add(css);

        oldPasswordTextField.setManaged(false);
        oldPasswordTextField.setVisible(false);

        newPasswordTextField.setManaged(false);
        newPasswordTextField.setVisible(false);

        confirmPasswordTextField.setManaged(false);
        confirmPasswordTextField.setVisible(false);

        oldPasswordPasswordField.textProperty().addListener((observableValue, s, t1) -> oldPasswordTextField.setText(t1));
        newPasswordPasswordField.textProperty().addListener((observableValue, s, t1) -> newPasswordTextField.setText(t1));
        confirmPasswordPasswordField.textProperty().addListener((observableValue, s, t1) -> confirmPasswordTextField.setText(t1));

    }

    public void setService(UtilizatorService service, Utilizator utilizator, Stage stage)
    {
        this.service = service;
        this.utilizator = utilizator;
        this.stage = stage;
    }

    @FXML
    private void changePassword()
    {
        String oldPassword = oldPasswordPasswordField.getText();
        String newPassword = newPasswordPasswordField.getText();
        String confirmPassword = confirmPasswordPasswordField.getText();

        if(service.checkPassword(utilizator, oldPassword) == true)
        {
            if(newPassword.equals(confirmPassword)) {
                service.savePassword(newPassword, utilizator);
                stage.close();
            }
            else
            {
                AlertController.showErrorMessage(null, "Passwords do not match!");
                newPasswordPasswordField.setStyle("-fx-border-color: red;");
                newPasswordTextField.setStyle("-fx-border-color: red;");

                confirmPasswordPasswordField.setStyle("-fx-border-color: red;");
                confirmPasswordTextField.setStyle("-fx-border-color: red;");
            }
        }
        else
        {
            AlertController.showErrorMessage(null, "Incorrect old password!");
            oldPasswordPasswordField.setStyle("-fx-border-color: red;");
            oldPasswordTextField.setStyle("-fx-border-color: red;");
        }
    }
}
