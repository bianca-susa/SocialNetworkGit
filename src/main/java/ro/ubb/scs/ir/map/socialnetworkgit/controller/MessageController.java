package ro.ubb.scs.ir.map.socialnetworkgit.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
//import ro.ubb.scs.ir.map.socialnetworkgit.MessageListCell;
import ro.ubb.scs.ir.map.socialnetworkgit.MessageView;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Message;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
import ro.ubb.scs.ir.map.socialnetworkgit.events.MessageChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.events.UtilizatorChangeEvent;
import ro.ubb.scs.ir.map.socialnetworkgit.observer.Observer;
import ro.ubb.scs.ir.map.socialnetworkgit.service.MessageService;
import ro.ubb.scs.ir.map.socialnetworkgit.service.UtilizatorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageController implements Observer<MessageChangeEvent> {
    private UtilizatorService service;

    private MessageService service_m;
    Utilizator utilizator;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ListView<Message> messageListView;

    @FXML
    private VBox messageContainer;


    @FXML
    private ChoiceBox<Utilizator> choiceBox;

    @FXML
    private TextArea messageInput;

    @FXML
    private Button sendButton;



    public void initialize()
    {
        Image image = new Image("plane_resize.png");
        sendButton.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public void setService(UtilizatorService service, MessageService service_m, Utilizator u)
    {
        this.service = service;
        this.service_m = service_m;
        service_m.addObserver(this);
        this.utilizator = u;

        if(null != u)
        {
            setFields(u);
        }
    }

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        System.out.println("Update method called in MessageController");
        showMessages(utilizator, choiceBox.getSelectionModel().getSelectedItem());
    }

    private void setFields(Utilizator u)
    {
        Iterable<Utilizator> allUsersIterable = service.getAll();
        List<Utilizator> allUsers = StreamSupport.stream(allUsersIterable.spliterator(), false)
                .collect(Collectors.toList());

        List<Utilizator> allFiltered = allUsers.stream().filter(x -> x.getId() != u.getId()).collect(Collectors.toList());

        choiceBox.getItems().addAll(allFiltered);


        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showMessages(utilizator, newValue);
            }
        });

    }

    public void sendMessage()
    {
        Utilizator sender = utilizator;
        Utilizator receiver = choiceBox.getSelectionModel().getSelectedItem();
        String text = messageInput.getText();

        List<Utilizator> list = new ArrayList<Utilizator>();
        list.add(receiver);
        Message message = new Message(sender, list, text, LocalDateTime.now());

        service_m.add(message);

        messageInput.clear();
    }


    private void showMessages(Utilizator u1, Utilizator u2) {
        List<Message> messages = service_m.getMessages(u1, u2);

        messageContainer.getChildren().clear();

        for (Message message : messages) {
            MessageView messageView = new MessageView(message, u1);
            messageContainer.getChildren().add(messageView);
        }

        scrollPane.vvalueProperty().bind(messageContainer.heightProperty());
    }


}
