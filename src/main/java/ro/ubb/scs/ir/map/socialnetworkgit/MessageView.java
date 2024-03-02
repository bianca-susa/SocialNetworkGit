package ro.ubb.scs.ir.map.socialnetworkgit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Message;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;

public class MessageView extends VBox {
    private Label userLabel;
    private TextField textField;

    private Label dataLabel;

    public MessageView(Message message, Utilizator currentUser) {
        // Create and configure UI elements
        userLabel = new Label(message.getFrom().getFirst_name());
        dataLabel = new Label(message.getData().toString());
        textField = new TextField(message.getMessage());

        userLabel.setVisible(true);
        dataLabel.setVisible(true);
        textField.setEditable(false); // Make it read-only



        // Set styles based on whether the message is from the current user
        if (message.getFrom().getId().equals(currentUser.getId())) {
            // Outgoing message (from the current user)
            setAlignment(Pos.CENTER_RIGHT);
            //textField.setStyle("-fx-background-color: #f3aedb;");
            textField.setStyle("-fx-background-color: #dc2352;");
            textField.setAlignment(Pos.CENTER_RIGHT);
        } else {

            setAlignment(Pos.CENTER_LEFT);
            textField.setStyle("-fx-background-color: #ecb2b2;");
            textField.setAlignment(Pos.CENTER_LEFT);
        }

        textField.setMaxWidth(computeTextWidth(textField.getFont(), textField.getText()));

        getChildren().addAll(userLabel, textField, dataLabel);

    }

    private double computeTextWidth(Font font, String text) {
        Text helper = new Text();
        helper.setFont(font);
        helper.setText(text);
        return helper.getBoundsInLocal().getWidth() + 20; // Add some padding
    }
}