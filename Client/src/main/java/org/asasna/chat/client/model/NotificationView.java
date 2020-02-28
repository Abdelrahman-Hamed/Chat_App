package org.asasna.chat.client.model;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.NotificationType;

import javax.swing.text.html.ImageView;

public class NotificationView extends VBox {
    private Client client;
    public NotificationView(Client client, Notification notification){
        if(notification.getType() == NotificationType.FRIEND_REQUEST){
            Circle circleImg = new Circle(20);
            circleImg.setFill(new ImagePattern(notification.getUser().getImage()));
            Text content = new Text(notification.getUser().getName() + " send Friend Request To You");
            content.setStyle("-fx-fill: white; -fx-font-size: 18px;");
            JFXButton accept = new JFXButton("Accept");
            JFXButton reject = new JFXButton("Reject");
            accept.setStyle("-jfx-button-type: RAISED;\n" +
                    "     -fx-background-color: #01ff41;\n" +
                    "     -fx-text-fill: white;" +
                    "-fx-padding: 5px 10px;" +
                    "-fx-font-size: 16px;");
            reject.setStyle("-jfx-button-type: RAISED;\n" +
                    "     -fx-background-color: #ff2f33;\n" +
                    "     -fx-text-fill: white;" +
                    "-fx-padding: 5px 10px;" +
                    "-fx-font-size: 16px;");
            accept.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    client.acceptRequest(notification.getUser().getId());
                    client.addFriend(notification.getUser());
                }
            });
            reject.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Notification View: " + notification.getUser().getId());
                    client.rejectFriendRequest(notification.getUser().getId());
                }
            });
            HBox userInfo = new HBox(circleImg, content);
            userInfo.setAlignment(Pos.CENTER);
            HBox btns = new HBox(accept, reject);
            btns.setAlignment(Pos.CENTER);
            btns.setSpacing(50);
            getChildren().addAll(userInfo, btns);
            setStyle("-fx-border-width: 0px 0px 1px 0px;" +
                    "-fx-border-color: white;" +
                    "-fx-border-style: solid");
        }
    }
}
