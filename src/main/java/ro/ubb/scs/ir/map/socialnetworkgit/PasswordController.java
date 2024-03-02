package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

public class PasswordController {
    @FXML
    private TextField enterTextField;

    @FXML
    private PasswordField passwordEnterField;

    @FXML
    private TextField confirmTextField;

    @FXML
    private PasswordField passwordConfirmField;

    @FXML
    private Button saveButton;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Label label;

    Utilizator utilizator;

    UtilizatorService utilizatorService;

    Stage dialogStage;
    private Runnable closeDialogCallback;

    @FXML
    private void initialize()
    {
        Image image = new Image("eye_resize.png");

        ImageView imageView = new ImageView(image);

        label.setGraphic(imageView);

        String css = getClass().getResource("views/styles.css").toExternalForm();
        toggleButton.getStylesheets().add(css);

        enterTextField.setManaged(false);
        enterTextField.setVisible(false);
        confirmTextField.setManaged(false);
        confirmTextField.setVisible(false);

        passwordEnterField.textProperty().addListener((observableValue, s, t1) -> enterTextField.setText(t1));
        passwordConfirmField.textProperty().addListener((observableValue, s, t1) -> confirmTextField.setText(t1));

        toggleButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (toggleButton.isSelected()) {
                enterTextField.setManaged(true);
                enterTextField.setVisible(true);
                passwordEnterField.setManaged(false);
                passwordEnterField.setVisible(false);

                confirmTextField.setManaged(true);
                confirmTextField.setVisible(true);
                passwordConfirmField.setManaged(false);
                passwordConfirmField.setVisible(false);
            } else {
                enterTextField.setManaged(false);
                enterTextField.setVisible(false);
                passwordEnterField.setManaged(true);
                passwordEnterField.setVisible(true);
                confirmTextField.setManaged(false);
                confirmTextField.setVisible(false);
                passwordConfirmField.setManaged(true);
                passwordConfirmField.setVisible(true);
            }
        });
    }

    public void setService(UtilizatorService service, Stage stage, Runnable closeDialogCallback,  Utilizator utilizator)
    {
        utilizatorService = service;
        this.utilizator = utilizator;
        this.closeDialogCallback = closeDialogCallback;
        this.dialogStage = stage;
    }

    @FXML
    public void createPassword(ActionEvent event)
    {
        String password1 = enterTextField.getText();
        String password2 = confirmTextField.getText();

        if(!password1.equals(password2))
        {
            AlertController.showErrorMessage(null, "Passwords must be identical!");
            enterTextField.setStyle("-fx-border-color: red;");
            confirmTextField.setStyle("-fx-border-color: red;");
            passwordConfirmField.setStyle("-fx-border-color: red;");
            passwordEnterField.setStyle("-fx-border-color: red;");
        }
        else
        {
            utilizatorService.savePassword(password1, utilizator);
            closeDialogCallback.run();
            dialogStage.close();
        }
    }

}
