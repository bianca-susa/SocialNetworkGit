package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.UtilizatorChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PageableImplementation;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
import ro.ubb.scs.ir.map.socialnetworkgit.service.CereriService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.MessageService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.PrieteniiService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UtilizatorChangeEvent> {

    String string;

    UtilizatorService service;

    MessageService srv_m;

    CereriService srv_c;

    PrieteniiService srv_p;

    int pageNumber;

    int pageSize;

    Pageable pageable;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    //List<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator, String> tableColumnNume;
    @FXML
    TableColumn<Utilizator, String> tableColumnPrenume;
    @FXML
    TableColumn<Utilizator, Void> actionColumn;
    @FXML
    Button addButton;
    @FXML
    Button nextButton;
    @FXML
    Button previousButton;
    @FXML
    TextField textField;
    @FXML
    Label pagesLabel;
    @FXML
    TextField pagesTextField;

    public void setUserService(UtilizatorService utilizatorService, MessageService srv_m, CereriService srv_c, PrieteniiService srv_p) {
        this.service = utilizatorService;
        this.srv_m = srv_m;
        this.srv_c = srv_c;
        this.srv_p = srv_p;
        pageNumber = 0;
        pageSize = 5;
        service.addObserver(this);
        handlePagination();
        initializePages();
        System.out.println("UserService set in UserController");
    }

    @Override
    public void update(UtilizatorChangeEvent utilizatorChangeEvent) {
        System.out.println("Update method called in UserController");
        initModel();
    }

    @FXML
    public void initialize() {
        tableView.setEditable(true);
        System.out.println("UserController initialized");
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("first_name"));
        tableColumnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("last_name"));

        Image image = new Image("plus_resize1.png");
        addButton.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Image image1 = new Image("next_resize.png");
        nextButton.setBackground(new Background(new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        Image image2 = new Image("previous_resize.png");
        previousButton.setBackground(new Background(new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        textField.setText("5");

        tableColumnNume.setPrefWidth(100);
        tableColumnPrenume.setPrefWidth(100);

        actionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null));
        actionColumn.setCellFactory(param -> new ro.ubb.scs.ir.map.socialnetworkgit.ButtonCell(service, this) {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && !isEmpty()) {
                    Utilizator user = getTableView().getItems().get(getIndex());
                    boolean showButtons = user != null;
                    setButtonsVisible(showButtons);
                } else {
                    setButtonsVisible(false);
                }
            }
        });



        tableView.getColumns().clear();
        tableView.getColumns().addAll(tableColumnNume, tableColumnPrenume, actionColumn);
        tableView.setItems(model);
    }

    @FXML
    private void selectPage()
    {
        try {
            pageNumber = Integer.valueOf(pagesTextField.getText());
            pageNumber = pageNumber - 1;
            handlePagination();
        }
        catch(NumberFormatException ex)
        {
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, "Trebuie sa introduceti un numar!");
        }
    }
    @FXML
    private void initializePages()
    {
        int number;
        long size = StreamSupport.stream(service.getAll().spliterator(), false).count();
        number = (int) (size / pageSize);
        if(size % pageSize != 0)
            number = number + 1;
        String text = Integer.toString(number);
        this.pagesLabel.setText("/" + text);

        number = pageNumber + 1;
        pagesTextField.setText(Integer.toString(number));
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        pageNumber++;
        handlePagination();
        initializePages();
    }

    @FXML
    private void handlePreviousPage(ActionEvent event) {
        if (pageNumber > 0) {
            pageNumber--;
            handlePagination();
            initializePages();
        }
    }

    @FXML
    private void handleTextChange()
    {
        try {
            String text = textField.getText();
            int number = Integer.valueOf(text);

            pageSize = number;
            pageNumber = 0;
            handlePagination();
            initializePages();
        }
        catch(NumberFormatException ex)
        {
            ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, "Trebuie sa introduceti un numar!");
        }
    }

    private void initModel() {
        pageable = new Pageable(pageNumber, pageSize);
        Page<Utilizator> usersPage = service.findAllOnPage(pageable);
        List<Utilizator> list = (List<Utilizator>) usersPage.getElementsOnPage();
        model.setAll(list);
    }

    public void handlePagination()
    {
        initModel();

        updatePaginationButtons();
    }

    private void updatePaginationButtons()
    {
        long size = StreamSupport.stream(service.getAll().spliterator(), false).count();
        boolean isFirstPage = pageNumber == 0;
        boolean isLastPage = pageNumber == size / pageSize;

        if(size % pageSize == 0)
            if( pageNumber == size/pageSize-1)
                isLastPage = true;

        previousButton.setVisible(!isFirstPage);
        nextButton.setVisible(!isLastPage);
    }
    public void handleAddUtilizator(ActionEvent actionEvent) {
        showEditDialog(null);

    }

    @FXML
    public void handleEditUtilizator(ActionEvent actionEvent) {
        Utilizator selected = (Utilizator) tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showEditDialog(selected);
        } else ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
    }

    public void handleDelete(ActionEvent actionEvent) {
        try {
            Utilizator selected = (Utilizator) tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Optional deleted = service.delete(selected.getId());
                if (deleted.isPresent())
                    ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Utilizatorul a fost sters cu succes!");
            } else ro.ubb.scs.ir.map.socialnetworkgit.controller.AlertController.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showEditDialog(Utilizator u) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/edit_view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ro.ubb.scs.ir.map.socialnetworkgit.controller.EditController editController = loader.getController();
            editController.setService(service, dialogStage, u);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessageController(Utilizator u) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/message_view.fxml"));

            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle("Message Controller");
            messageStage.initModality(Modality.WINDOW_MODAL);
            messageStage.setScene(new Scene(root));

            ro.ubb.scs.ir.map.socialnetworkgit.controller.MessageController messageController = loader.getController();
            messageController.setService(service, srv_m, u);

            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCreatePasswordController(Utilizator u) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/create_view.fxml"));

            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle("Password");
            messageStage.initModality(Modality.WINDOW_MODAL);
            messageStage.setScene(new Scene(root));

            CreatePasswordController passwordController = loader.getController();
            passwordController.setService(service, u);

            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCerereController(Utilizator u) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/cerere_view.fxml"));

            Parent root = loader.load();

            Stage cerereStage = new Stage();
            cerereStage.setTitle("Friendship Controller");
            cerereStage.initModality(Modality.WINDOW_MODAL);
            cerereStage.setScene(new Scene(root));

            ro.ubb.scs.ir.map.socialnetworkgit.controller.CerereController cerereController = loader.getController();
            cerereController.setService(service, srv_c,  u);

            cerereStage.show();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showFriendsController(Utilizator u) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/friends_view.fxml"));

            Parent root = loader.load();

            Stage cerereStage = new Stage();
            cerereStage.setTitle("Friendship Controller");
            cerereStage.initModality(Modality.WINDOW_MODAL);
            cerereStage.setScene(new Scene(root));

            ro.ubb.scs.ir.map.socialnetworkgit.controller.FriendsController friendsController = loader.getController();
            friendsController.setService(service, srv_c, srv_p,this, u);

            cerereStage.show();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showLogInController(Utilizator u) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/login_view.fxml"));

            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle(u.getFirst_name() + " " + u.getLast_name() + " " + "Log In");
            messageStage.initModality(Modality.WINDOW_MODAL);
            messageStage.setScene(new Scene(root));

            LogInController loginController = loader.getController();
            loginController.setService(service,u, messageStage,this);

            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangePasswordController(Utilizator u) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/change_view.fxml"));

            Parent root = loader.load();

            Stage messageStage = new Stage();
            messageStage.setTitle(u.getFirst_name() + " " + u.getLast_name() + " " + "Change Password");
            messageStage.initModality(Modality.WINDOW_MODAL);
            messageStage.setScene(new Scene(root));

            ChangePasswordController loginController = loader.getController();
            loginController.setService(service,u, messageStage);

            messageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

