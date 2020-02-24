package org.asasna.chat.client.model;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

public class Contact extends HBox {
    private String name;
    private Image image;
    private UserStatus status;
    private User user;
    private boolean isGroup = false;

    public Contact() {
    }

    public Contact(String name, Image image, UserStatus status) {
        this.name = name;
        this.image = image;
        this.status = status;
        Circle circle = new Circle();
        circle.setRadius(30);
        if (image != null)
            circle.setFill(new ImagePattern(image));
        circle.setCenterY(75);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 18;-fx-font-weight: 500;-fx-text-fill: WHITE;-fx-padding: 5 0 0 0;");
        HBox hBox = new HBox(5);
        Circle statusCircle = new Circle(5);
        statusCircle.setStyle("-fx-fill:  #33FF4B");
        Label statusLabel = new Label("Online");
        statusLabel.setStyle("-fx-text-fill: White");
        hBox.getChildren().addAll(statusCircle, statusLabel);
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(nameLabel, hBox);
        getChildren().addAll(circle, vBox);
    }

    public Contact(User user) {
        this.user = user;
        this.name = user.getName();
        this.image = user.getImage();
        this.status = user.getStatus();
        Circle circle = new Circle();
        circle.setRadius(30);
        if (image != null)
            circle.setFill(new ImagePattern(image));
        circle.setCenterY(75);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-family: 'Comic Sans MS';-fx-font-size: 18;-fx-font-weight: 500;-fx-text-fill: WHITE;-fx-padding: 5 0 0 0;");
        HBox hBox = new HBox(5);
        Circle statusCircle=null;
        Label statusLabel=null;
        switch (status) {
            case ONLINE:
                statusCircle = new Circle(5);
                statusCircle.setStyle("-fx-fill:  #33FF4B");
                statusLabel = new Label("Online");
                statusLabel.setStyle("-fx-text-fill: White");
                break;
            case BUSY:
                statusCircle = new Circle(5);
                statusCircle.setStyle("-fx-fill:  #FF8C00");
                statusLabel = new Label("Busy");
                statusLabel.setStyle("-fx-text-fill: White");
                break;
            case OFFLINE:
                statusCircle = new Circle(5);
                statusCircle.setStyle("-fx-fill:  #A9A9A9");
                statusLabel = new Label("Offline");
                statusLabel.setStyle("-fx-text-fill: White");
                break;
        }

        hBox.getChildren().addAll(statusCircle, statusLabel);
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(nameLabel, hBox);
        getChildren().addAll(circle, vBox);
    }

    public Image getImage() {
        return image;
    }

    {
        setSpacing(20);
        getStyleClass().add("contact");
        /*setStyle("-fx-background-color:  #1e82dc;\n" +
                "    " +
                "-fx-padding: 10 0 10 15; -fx-border-width: 2px;\n" +
                "    -fx-border-color: #1e82dc #1e82dc white #1e82dc");*/
        setHeight(150);
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
