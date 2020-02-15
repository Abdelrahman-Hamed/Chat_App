package org.asasna.chat.client.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

import java.io.FileInputStream;
import java.io.IOException;

public class SearchedContact extends Contact {

    public SearchedContact(String name, Image image, UserStatus userStatus){
        super(name, image, userStatus);


        Button addBtn = new Button("+");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Button Clicked");
            }
        });
        getChildren().add(addBtn);
    }
    public SearchedContact(User user) throws IOException {
        this(user.getName(), new Image(new FileInputStream("./client/src/main/resources/org/asasna/chat/client/abdo.jpg")), user.getStatus());
    }
}
