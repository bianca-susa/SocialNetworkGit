package ro.ubb.scs.ir.map.socialnetworkgit.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.CerereChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

public class CerereController implements Observer<CerereChangeEvent> {
    private UtilizatorService service;

    private CereriService service_c;
    Utilizator utilizator;

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator, String> tableColumnNume;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume;
    @FXML
    TableColumn<Utilizator, Void> actionColumn;

    public void setService(UtilizatorService service, CereriService service_c, Utilizator u)
    {
        this.service = service;
        this.service_c = service_c;
        this.utilizator = u;
        service_c.addObserver(this);

        initModel();
    }

    @Override
    public void update(CerereChangeEvent cerereChangeEvent) {
        initModel();
    }

    @FXML
    public void initialize() {
        tableView.setEditable(true);
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("first_name"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("last_name"));


        tableColumnNume.setPrefWidth(100);
        tableColumnPrenume.setPrefWidth(100);

        actionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));
        actionColumn.setCellFactory(param -> new AddCell(service_c, this, utilizator));



        tableView.getColumns().clear();
        tableView.getColumns().addAll(tableColumnNume, tableColumnPrenume, actionColumn);
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> users = service.getAll();
        model.clear();

        for (Utilizator user : users) {
            // Check if there is no friendship between the current user and the user in the iteration
            if (service_c.findOne(utilizator, user) == null && service_c.findOne(user, utilizator)==null && !utilizator.equals(user)) {
                model.add(user);
            }
        }

    }
}
