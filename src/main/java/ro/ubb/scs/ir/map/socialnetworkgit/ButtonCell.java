package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.io.IOException;
import java.util.Optional;

public class ButtonCell extends TableCell<Utilizator, Void> {
    private final Button deleteButton = new Button("", new ImageView("trash_resize2.png"));

    private final Button updateButton = new Button("", new ImageView("pen_resize1.png"));

    private final Button logInButton = new Button("", new ImageView("account_resize.png"));

    private UtilizatorService service;
    private UserController controller;

    public ButtonCell(UtilizatorService service, UserController controller) {
        this.service = service;
        this.controller = controller;

        deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
        updateButton.setOnAction(event -> handleUpdate(getTableRow().getItem()));

        logInButton.setOnAction(event -> {
            try {
                handleLogIn(getTableRow().getItem());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        HBox buttonBox = new HBox(deleteButton, updateButton, logInButton);
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER);


        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(buttonBox);
    }

    private void handleDelete(Utilizator utilizator) {

        if (utilizator != null) {
            Optional deleted = service.delete(utilizator.getId());
            if (deleted.isPresent())
                AlertController.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Utilizatorul a fost sters cu succes!");
        } else AlertController.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
    }

    private void handleUpdate(Utilizator utilizator) {
        controller.showEditDialog(utilizator);
    }

    private void handleLogIn(Utilizator utilizator) throws IOException {
        String password = service.getUserPassword(utilizator);

        if(password == null)
            controller.showCreatePasswordController(utilizator);
        else
            controller.showLogInController(utilizator);
    }

    private void handleSend(Utilizator utilizator) throws IOException {
        controller.showMessageController(utilizator);
    }

    public void setButtonsVisible(boolean visible) {
        deleteButton.setVisible(visible);
        updateButton.setVisible(visible);
        logInButton.setVisible(visible);
    }

    private void handleFriends(Utilizator utilizator)
    {
        controller.showCerereController(utilizator);
    }

    private void showFriends(Utilizator utilizator)
    {
        controller.showFriendsController(utilizator);
    }
}

