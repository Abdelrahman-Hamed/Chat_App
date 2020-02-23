package org.asasna.chat.client.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.NotificationType;

public class NotificationView extends VBox {
    private Client client;
    public NotificationView(Client client, Notification notification){
        if(notification.getType() == NotificationType.FRIEND_REQUEST){
            Text content = new Text(notification.getUser().getName() + " send Friend Request To You");
            Button accept = new Button("Accept");
            Button reject = new Button("Reject");
            accept.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    client.acceptRequest(notification.getUser().getId());
                }
            });
            reject.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("Notification View: " + notification.getUser().getId());
                    client.rejectFriendRequest(notification.getUser().getId());
                }
            });
            HBox btns = new HBox(accept, reject);
            btns.setSpacing(50);
            getChildren().addAll(content, btns);
        }
    }
}
