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
import ro.ubb.scs.ir.map.socialnetworkgit.events.PrietenieChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.CerereChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;

public class HandleRequestCell extends TableCell<Utilizator, Void> implements Observer<CerereChangeEvent> {
    private final Button acceptButton = new Button("", new ImageView("add_resize2.png"));

    private final Button denyButton = new Button("", new ImageView("deny_resize.png"));

    private CereriService service;

    Utilizator utilizator;
    private ro.ubb.scs.ir.map.socialnetworkgit.controller.FriendsController controller;

    public HandleRequestCell(CereriService service, ro.ubb.scs.ir.map.socialnetworkgit.controller.FriendsController controller, Utilizator u) {
        this.service = service;
        service.addObserver(this);
        this.controller = controller;
        this.utilizator = u;

        acceptButton.setOnAction(event -> acceptRequest(getTableRow().getItem()));
        denyButton.setOnAction(event -> denyRequest(getTableRow().getItem()));

        HBox buttonBox = new HBox(acceptButton, denyButton);
        buttonBox.setSpacing(5);
        buttonBox.setAlignment(Pos.CENTER);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(buttonBox);
    }

    @Override
    public void update(CerereChangeEvent cerereChangeEvent) {
        controller.initModel();
    }

    public void setButtonsVisible(boolean visible) {
        acceptButton.setVisible(visible);
        denyButton.setVisible(visible);
    }

    private void acceptRequest(Utilizator friend)
    {
        //utilizator a primit, friend a trimis

        Cerere cerere = new Cerere();
        cerere.setId(new Tuple<>(friend, utilizator));
        cerere.setStatus("accepted");

        service.updateRequest(cerere);

        controller.addFriend(friend);
    }

    private void denyRequest(Utilizator friend)
    {
        Cerere cerere = new Cerere();
        cerere.setId(new Tuple<>(friend, utilizator));
        cerere.setStatus("rejected");

        service.updateRequest(cerere);
    }
}

