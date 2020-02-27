package org.asasna.chat.client.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import org.asasna.chat.client.view.SpeechDirection;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.User;

import java.sql.Timestamp;

public class MessageView extends HBox {
    private SpeechDirection direction;
    private Text displayedText;
    private Message message ;
    private Image image;
    private Timestamp timestamp;

    public MessageView(Message message){
        this.message = message;

    }

    public Text getDisplayedText(){
        return displayedText;
    }

    public void setDirection(SpeechDirection direction) {
        this.direction = direction;
        setupElements();
    }

    private void setupElements(){
        if (direction == SpeechDirection.RIGHT){
            configureForSender();
        } else {
            configureForReceiver();
        }
    }

    private void setCommonConfigurations(){
        timestamp = new Timestamp(System.currentTimeMillis());
        displayedText = new Text(message.getMesssagecontent() + timestamp.getDate()+":"
                + timestamp.getMinutes());
        displayedText.setTextAlignment(TextAlignment.LEFT);
        displayedText.setFont(Font.font("Consolas",FontPosture.REGULAR, 16));
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void configureForSender(){
        setCommonConfigurations();
        displayedText.setFill(Color.WHITE);
        TextFlow textFlow = new TextFlow(displayedText);
        HBox container = new HBox(textFlow);
        container.setStyle("-fx-background-radius: 15; -fx-background-color: #1e82dc ");
        container.setAlignment(Pos.CENTER_LEFT);
        container.maxWidthProperty().bind(widthProperty().multiply(0.70));
        container.setMargin(textFlow, new Insets(10,10,15,10));
        getChildren().add(container);
        setAlignment(Pos.CENTER_RIGHT);
        this.setMargin(container, new Insets(5,10,10,10));

    }

    public void configureForReceiver(){
        setCommonConfigurations();
        displayedText.setTextAlignment(TextAlignment.LEFT);
        displayedText.setFill(Color.BLACK);
        Circle circle = new Circle();
        circle.setRadius(20);
        if (image != null) {
            circle.setFill(new ImagePattern(image));
        }
        circle.setCenterY(75);
        TextFlow textFlow = new TextFlow(displayedText);
        HBox container = new HBox(circle, textFlow);
        container.setStyle("-fx-background-radius: 15; -fx-background-color: GAINSBORO ");
        container.setAlignment(Pos.CENTER_LEFT);
        container.maxWidthProperty().bind(widthProperty().multiply(0.70));
        container.setMargin(textFlow, new Insets(10,10,15,10));
        getChildren().addAll(circle, container);
        setAlignment(Pos.CENTER_LEFT);
        this.setMargin(container, new Insets(5,10,10,10));
    }
}
