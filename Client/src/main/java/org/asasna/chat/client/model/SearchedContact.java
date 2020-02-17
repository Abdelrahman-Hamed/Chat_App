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
    private String userPhone;
    private Client client;

    public SearchedContact(String name, Image image, UserStatus userStatus) {
        super(name, image, userStatus);

        Button addBtn = new Button("+");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                client.sendFriendRequest(userPhone);
            }
        });
        getChildren().add(addBtn);
    }

    public SearchedContact(Client client, User user) {
        this(user.getName(), user.getImage(), user.getStatus());
        userId = user.getId();
        userPhone = user.getPhone();
        this.client = client;
    }

}
