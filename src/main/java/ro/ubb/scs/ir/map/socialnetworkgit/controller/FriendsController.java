package ro.ubb.scs.ir.map.socialnetworkgit.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Pair;
import ro.ubb.scs.ir.map.socialnetworkgit.UserController;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Prietenie;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.CerereChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.PrietenieChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.PrieteniiService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.io.IOException;
import java.time.LocalDate;

public class FriendsController implements Observer<PrietenieChangeEvent> {
    private UtilizatorService service;

    private CereriService service_c;

    private PrieteniiService service_p;

    private UserController userController;
    Utilizator utilizator;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();


    @FXML
    TableView<Utilizator> tableView_cereri;

    @FXML
    TableView<Utilizator> tableView_friends;
    @FXML
    TableColumn<Utilizator, String> tableColumnNume_c;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume_c;
    @FXML
    TableColumn<Utilizator, Void> actionColumn;


    @FXML
    TableColumn<Utilizator, String> tableColumnNume_f;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume_f;
    @FXML
    TableColumn<Utilizator,LocalDate> friendsSince;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button messagesButton;


    public void setService(UtilizatorService service, CereriService service_c, PrieteniiService service_p, UserController controller,  Utilizator u)
    {
        this.service = service;
        this.service_c = service_c;
        this.service_p = service_p;
        this.userController = controller;
        this.utilizator = u;
        service_p.addObserver(this);

        Image image1 = new Image("mail_resize1.png");
        messagesButton.setBackground(new Background(new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Image image2 = new Image("add_resize.png");
        addFriendButton.setBackground(new Background(new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        initModel();
        initModelFriends();
    }

    @Override
    public void update(PrietenieChangeEvent prietenieChangeEvent) {
        initModelFriends();
    }

    @FXML
    public void initialize() {
        tableView_cereri.setEditable(true);
        tableColumnNume_c.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("first_name"));
        tableColumnPrenume_c.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("last_name"));

        tableColumnNume_c.setPrefWidth(100);
        tableColumnPrenume_c.setPrefWidth(100);

        actionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));
        actionColumn.setCellFactory(param -> new HandleRequestCell(service_c, this, utilizator) {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && !isEmpty()) {
                    Utilizator user = getTableView().getItems().get(getIndex());
                    Cerere cerere = (Cerere) service_c.findOne(user, utilizator);
                    boolean showButtons = cerere != null && cerere.getStatus().equals("pending");
                    setButtonsVisible(showButtons);
                } else {
                    setButtonsVisible(false);
                }
            }
        });

        tableView_cereri.getColumns().clear();
        tableView_cereri.getColumns().addAll(tableColumnNume_c, tableColumnPrenume_c, actionColumn);
        tableView_cereri.setItems(model);

        tableView_friends.setEditable(true);
        tableColumnNume_f.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("first_name"));
        tableColumnPrenume_f.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("last_name"));

        friendsSince.setCellValueFactory(cellData ->
        {
            Utilizator friend = cellData.getValue();
            Prietenie prietenie = (Prietenie) service_p.find(utilizator, friend);

            if(prietenie != null)
            {
                return new SimpleObjectProperty<>(prietenie.getDate());
            }
            else
            {
                return new SimpleObjectProperty<>(null);
            }
        });


        tableColumnNume_f.setPrefWidth(100);
        tableColumnPrenume_f.setPrefWidth(100);
        friendsSince.setPrefWidth(100);


        tableView_friends.getColumns().clear();
        tableView_friends.getColumns().addAll(tableColumnNume_f, tableColumnPrenume_f, friendsSince);
        tableView_friends.setItems(modelFriends);
    }

    void initModel() {
        Iterable<Utilizator> users = service.getAll();
        model.clear();

        for (Utilizator user : users) {
            Cerere cerere = (Cerere) service_c.findOne(user,utilizator);

            if (cerere != null) {
                if(cerere.getStatus().equals("pending"))
                    model.add(user);
            }
        }
    }

    private void initModelFriends() {
        Iterable<Utilizator> users = service.getAll();
        modelFriends.clear();

        for (Utilizator user : users) {
            // Check if there is no friendship between the current user and the user in the iteration
            Prietenie prietenie = (Prietenie) service_p.find(utilizator,user);
            if (prietenie != null) {
                modelFriends.add(user);
            }
        }
    }

    public void addFriend(Utilizator friend)
    {
        Prietenie prietenie = new Prietenie(LocalDate.now());
        prietenie.setId(new Tuple<>(utilizator, friend));
        service_p.add(prietenie);

    }

    @FXML
    private void handleMessages() throws IOException {
        userController.showMessageController(utilizator);
    }

    @FXML
    private void handleFriends()
    {
        userController.showCerereController(utilizator);
    }
}

