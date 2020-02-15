package org.asasna.chat.client.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.model.Contact;
import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.client.model.MSGview;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.client.model.SearchedContact;
import org.asasna.chat.common.model.*;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable, IChatController {

    Client client ;
    @FXML
    TextField searchTextField;

    @FXML
    TextArea messageTextArea;
    @FXML
    FontIcon profileIcon, groupIcon, logoutIcon, addFriendIcon, notificationIcon, saveChatIcon;

    @FXML
    VBox contactsList;
    @FXML
    VBox view ;

    MSGview viewTextMessage;
    private User me;


    public Contact activeContact;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setToolTip();
        me = new User();
        try {
            Contact contact = new Contact("Abdelrahman", new Image(new FileInputStream("./client/src/main/resources/org/asasna/chat/client/abdo.jpg")), UserStatus.ONLINE);
            contactsList.getChildren().add(contact);
            SearchedContact searchedContact = new SearchedContact("Sayed Nabil", new Image(new FileInputStream("./client/src/main/resources/org/asasna/chat/client/abdo.jpg")), UserStatus.ONLINE);
            contactsList.getChildren().add(searchedContact);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void send() {
        String message = messageTextArea.getText();
        messageTextArea.setText("");
        System.out.println(message);
    }

    public void sendAudio() {

    }

    public void saveChat() {

    }

    public void updateProfile() {

    }

    public void addContact() {

    }

    public void createGroupChat() {

    }

    public void changeStatus() {

    }

    public void showNotifications() {

    }

    public void enableChatBot() {

    }

    public void logout() {

    }

    public void exit() {

    }
    @FXML
    private void getSelectedContact() {
        ObservableList<Node> contacts;
        contacts = contactsList.getChildren() ;
        for (Node c:contacts) {
            c.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.activeContact = (Contact) c;
              //  System.out.println("active Contact is : "+ this.activeContact.getUser().getName());
            });
        }

    }

    private void setToolTip() {
        Tooltip profileTooltip, groupTooltip, addFriendTooltip, notificationTooltip, saveChatTooltip, logoutTooltip;
        profileTooltip = new Tooltip("Profile");
        groupTooltip = new Tooltip("Group Chat");
        addFriendTooltip = new Tooltip("Add Friend");
        notificationTooltip = new Tooltip("Notification");
        saveChatTooltip = new Tooltip("Save Chat");
        logoutTooltip = new Tooltip("Logout");

        profileTooltip.setStyle("-fx-font-size: 14");
        groupTooltip.setStyle("-fx-font-size: 14");
        addFriendTooltip.setStyle("-fx-font-size: 14");
        notificationTooltip.setStyle("-fx-font-size: 14");
        saveChatTooltip.setStyle("-fx-font-size: 14");
        logoutTooltip.setStyle("-fx-font-size: 14");

        Tooltip.install(profileIcon, profileTooltip);
        Tooltip.install(groupIcon, groupTooltip);
        Tooltip.install(addFriendIcon, addFriendTooltip);
        Tooltip.install(notificationIcon, notificationTooltip);
        Tooltip.install(saveChatIcon, saveChatTooltip);
        Tooltip.install(logoutIcon, logoutTooltip);

    }


    @Override
    public void displayMessage(Message msg) {
        viewTextMessage = new MSGview(msg);
        if(me.getId() == msg.getUserId()){
            viewTextMessage.setTextMSGview(SpeechDirection.RIGHT);

        }
        else {
            viewTextMessage.setTextMSGview(SpeechDirection.LEFT);
        }
    }

    @Override
    public void changeStatus(UserStatus status) {

    }

    @Override
    public void recieveNotification(Notification notification) {

    }


    //    Start Elsayed Nabil

    public void searchContacts(KeyEvent keyEvent) {
        String searchedMessage = searchTextField.getText();
//        List<User> users = client.search(searchedMessage);
        List<User> users = new ArrayList<>();
        String names[] = {"Elsayed Nabil", "Abeer Emad", "Abdo Fahmy", "Aya Amin", "Shymaa shokry"};
        for (int i = 0; i < names.length; i++) {
            users.add(new User(names[i], "01279425232", "sayed0nabil@gmail.com", "123456789", Gender.Male, "Egypt", null, null, UserStatus.ONLINE, "abdo.jpg", false, false));
        }
        contactsList.getChildren().clear();
        users.forEach(user -> {
            try {
                contactsList.getChildren().add(new SearchedContact(client, user));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    // End Elsayed Nabil


    //    Start Abdo
    // End Abdo




    //    Start Aya
    // End Aya




    //    Start Shimaa
    // End shimaa



    //    Start Abeer Emad
    // End Abeer Emad



    //    Start Nehal Adel
    // End Nehal Adel
}
