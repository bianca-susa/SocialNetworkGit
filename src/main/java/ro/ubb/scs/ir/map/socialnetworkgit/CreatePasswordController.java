package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.io.IOException;

public class CreatePasswordController {
    @FXML
    Button createButton;

    Utilizator utilizator;

    UtilizatorService service;



    public void setService(UtilizatorService service, Utilizator u)
    {
        this.service = service;
        this.utilizator = u;
    }

    public void handleCreate() throws IOException {
        showPasswordController(utilizator);
    }


    public void showPasswordController(Utilizator u) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/password_view.fxml"));

            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle("Password" + utilizator.getFirst_name() + utilizator.getLast_name());
            messageStage.initModality(Modality.WINDOW_MODAL);
            messageStage.setScene(new Scene(root));

            PasswordController passwordController = loader.getController();
            passwordController.setService(service, messageStage, this::closeStage, u);

            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        ((Stage) createButton.getScene().getWindow()).close();
    }
}
