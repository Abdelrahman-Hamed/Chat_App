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
    private boolean friendRequest;
    private  void setAddButton(Button btn){
        btn.setText("Add");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean done = client.sendFriendRequest(userId);
                if(done) {
                    friendRequest = !friendRequest;
                    setCancelButton(btn);
                }
            }
        });
    }
    private void setCancelButton(Button btn){
        btn.setText("Cancel");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                boolean done = client.cancelFriendRequest(userId);
                if(done) {
                    friendRequest = !friendRequest;
                    setAddButton(btn);
                }
            }
        });
    }
    private Button handleAction(){
        System.out.println("Get In Function");
         Button btn = new Button();
         if(friendRequest){
             setCancelButton(btn);
         }else{
            setAddButton(btn);
         }
         return btn;
    }
    public SearchedContact(String name, Image image, UserStatus userStatus, boolean friendRequest) {
        super(name, image, userStatus);
        this.friendRequest = friendRequest;
        Button btn = handleAction();
        getChildren().add(btn);
    }

    public SearchedContact(Client client, User user, boolean friendRequest) {
        this(user.getName(), user.getImage(), user.getStatus(), friendRequest);
        userId = user.getId();
        this.client = client;
    }

}

