package org.asasna.chat.client.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

import java.io.FileInputStream;
import java.io.IOException;

public class SearchedContact extends Contact {
    private int userId;
    private Client client;

    public SearchedContact(String name, Image image, UserStatus userStatus) {
        super(name, image, userStatus);
        Button addBtn = new Button("+");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean done = client.sendFriendRequest(userId);
                if(done) addBtn.setText("-");
            }
        });
        getChildren().add(addBtn);
    }
    public void setClient(Client client){
        this.client = client;
    }
    public SearchedContact(Client client, User user) {
        this(user.getName(), null, user.getStatus());
        userId = user.getId();
        this.client = client;
    }

}
