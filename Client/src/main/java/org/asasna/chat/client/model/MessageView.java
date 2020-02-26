package org.asasna.chat.client.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.asasna.chat.client.view.SpeechDirection;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.User;

public class MessageView extends HBox {
    private SpeechDirection direction;
    private Label displayedText;
    private Message message ;
    private Image image;

    public MessageView(Message message){
        this.message = message;

    }
    public Label getDisplayedText(){
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
        displayedText = new Label(message.getMesssagecontent());
        displayedText.setPadding(new Insets(10));
        displayedText.setWrapText(true);
    }

    public void setImage(Image image){
        this.image = image;
    }

    public void configureForSender(){
        setCommonConfigurations();
        displayedText.setStyle("-fx-background-color: DodgerBlue; " +
                "-fx-background-radius: 20;-fx-font-size: 16px; -fx-font-family: 'Consolas';");
        displayedText.setTextFill(Color.WHITE);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        HBox container = new HBox(displayedText);
        container.maxWidthProperty().bind(widthProperty().multiply(0.65));
        container.setMargin(displayedText, new Insets(5,10,10,10));
        getChildren().add(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    public void configureForReceiver(){
        setCommonConfigurations();
        displayedText.setStyle("-fx-background-color: Gainsboro; -fx-background-radius: 20;" +
                "-fx-background-radius: 20;-fx-font-size: 16px; -fx-font-family: 'Consolas';");
        displayedText.setAlignment(Pos.CENTER_LEFT);
        Circle circle = new Circle();
        circle.setRadius(20);
        if (image != null) {
            circle.setFill(new ImagePattern(image));
        }
        circle.setCenterY(75);
        HBox container = new HBox(circle, displayedText);
        container.setAlignment(Pos.CENTER_LEFT);
        container.maxWidthProperty().bind(widthProperty().multiply(0.65));
        container.setMargin(displayedText, new Insets(5,10,10,10));
        getChildren().add(container);
        setAlignment(Pos.CENTER_LEFT);
    }
}
