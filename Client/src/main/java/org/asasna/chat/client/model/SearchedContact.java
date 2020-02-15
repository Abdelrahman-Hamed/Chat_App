package org.asasna.chat.client.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.asasna.chat.common.model.UserStatus;

public class SearchedContact extends VBox {
    public SearchedContact(String name, Image image, UserStatus userStatus){
        Contact contact = new Contact(name, image, userStatus);
        HBox btnContainer = new HBox();
        btnContainer.setAlignment(Pos.CENTER_RIGHT);

        Button addBtn = new Button("+");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        btnContainer.getChildren().add(addBtn);
        btnContainer.setPadding(new Insets(0, 20, 0, 0));
        getChildren().addAll(contact, btnContainer);
    }
}
