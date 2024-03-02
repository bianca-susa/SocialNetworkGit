package ro.ubb.scs.ir.map.socialnetworkgit.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;

public class AddCell extends TableCell<Utilizator, Void> {
    private final Button addButton = new Button("", new ImageView("plus_resize2.png"));

    private CereriService service;

    Utilizator utilizator;
    private CerereController controller;

    public AddCell(CereriService service, CerereController controller, Utilizator u) {
        // Set up button actions
        this.service = service;
        this.controller = controller;
        this.utilizator = u;

        addButton.setOnAction(event -> sendCerere(getTableRow().getItem()));

        HBox buttonBox = new HBox(addButton);
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(buttonBox);
    }

    public void setButtonsVisible(boolean visible) {
        addButton.setVisible(visible);
    }


    private void sendCerere(Utilizator friend)
    {
        Cerere cerere = new Cerere();
        cerere.setId(new Tuple<>(utilizator, friend));
        cerere.setStatus("pending");
        this.service.add(cerere);


    }
}
