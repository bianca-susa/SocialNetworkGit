package ro.ubb.scs.ir.map.socialnetworkgit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.ValidationException;

import java.util.Optional;

import static java.lang.Long.parseLong;

public class EditController {
    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldPrenume;

    private UtilizatorService service;

    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    private void initialize()
    {}

    public void setService(UtilizatorService service, Stage stage, Utilizator u)
    {
        this.service = service;
        this.dialogStage = stage;
        this.utilizator = u;

        if(null != u)
        {
            setFields(u);
        }
    }

    private void setFields(Utilizator u)
    {
        textFieldNume.setText(u.getFirst_name());
        textFieldPrenume.setText(u.getLast_name());
    }

    private void clearFields()
    {
        textFieldNume.setText("");
        textFieldPrenume.setText("");
    }

    @FXML
    public void handleSave()
    {
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();

        Utilizator u = new Utilizator(nume,prenume);

        if(this.utilizator != null)
        {
            u.setId(utilizator.getId());
            this.updateUser(u);
        }
        else {
            this.saveUser(u);
        }
    }

    private void saveUser(Utilizator u)
    {
        try
        {
            Optional o = this.service.add(u);
            if(o.isEmpty())
                dialogStage.close();
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showMessage(null, Alert.AlertType.INFORMATION, "Salvare", "Utilizatorul a fost salvat!");
        }
        catch(ValidationException e)
        {
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateUser(Utilizator u)
    {
        try
        {
            Optional o = this.service.update(u);
            if(o.isEmpty())
                dialogStage.close();
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showMessage(null, Alert.AlertType.INFORMATION, "Update","Utilizator salvat!");
        }
        catch (ValidationException e)
        {
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, e.getMessage());
        }
    }


    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
