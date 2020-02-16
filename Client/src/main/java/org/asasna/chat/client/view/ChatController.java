package org.asasna.chat.client.view;

import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChatController implements Initializable, IChatController {

    Client client;
    @FXML
    TextField searchTextField;

    @FXML
    TextArea messageTextArea;
    @FXML
    FontIcon profileIcon, groupIcon, logoutIcon, addFriendIcon, notificationIcon, saveChatIcon;

    @FXML
    VBox contactsList;
    @FXML
    VBox view;

    MSGview viewTextMessage;
    private User me;


    public Contact activeContact;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setToolTip();
        user = new User(4, "Ahmed", "01027420575");
        Message message = new Message(3, "Hello");
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(new User(1, "Khaled", "014587"));
        list.add(new User(5, "Sayed", "54663"));
        ChatGroup chatGroup = new ChatGroup(1, list.stream().map(u -> u.getId()).collect(Collectors.toList()), "Group1");
        try {
            client.sendGroupMessage(chatGroup, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
    public void setClient(Client client){
        this.client = client;
    }

    @FXML
    private void getSelectedContact() {
        ObservableList<Node> contacts;
        contacts = contactsList.getChildren();
        for (Node c : contacts) {
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
        if (me.getId() == msg.getUserId()) {
            viewTextMessage.setTextMSGview(SpeechDirection.RIGHT);
            HBox drawer = new HBox();
            Image im = activeContact.getImage();
            Circle circle = new Circle();
            circle.setRadius(20);
            circle.setFill(new ImagePattern(im));
            circle.setCenterY(75);
            view.getChildren().add(circle);
            view.getChildren().add(viewTextMessage);
        } else {
            viewTextMessage.setTextMSGview(SpeechDirection.LEFT);
            Image im = activeContact.getImage();
            Circle circle = new Circle();
            circle.setRadius(20);
            circle.setFill(new ImagePattern(im));
            circle.setCenterY(75);
            view.getChildren().add(circle);
            view.getChildren().add(viewTextMessage);
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
        List<User> users = client.search(searchedMessage);
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
    public void recieveGroupMessage(ChatGroup group, Message message) {
        System.out.println("Message from group : ");
        System.out.println(message.getMesssagecontent());
        System.out.println("Group : " + group.getName());
        Platform.runLater(() -> {
            Contact contact = new Contact(group.getName(), null, UserStatus.ONLINE);
            contact.setOnMouseClicked((e) -> {
                this.activeContact = contact;
                System.out.println(activeContact.getName());
            });
            contactsList.getChildren().add(contact);
        });
    }


    public void sendGroupMessage(ChatGroup group, Message message) throws RemoteException {
        client.sendGroupMessage(group, message);

    }
    // End Abdo


    //    Start Aya
    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        //validation
        if (selectedFile != null) {
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            new Thread(() -> {
                try {
                    client.sendFileToServer(selectedFile.getPath(), fileExtension);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    // End Aya


    //    Start Shimaa
    @Override
    public void sendMessage(int receiverId, Message message) {
        try {
            client.sendMessage(receiverId, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    // End shimaa


    //    Start Abeer Emad
    // End Abeer Emad


    //    Start Nehal Adel

    public void send() {
        String messageTXT = messageTextArea.getText();
        Message mes = new Message(5, messageTXT);
        messageTextArea.setText("");
        displayMessage(mes);
        System.out.println(messageTXT);

        /* Lines added by Shimaa */
        int friendId = Integer.parseInt(activeContact.getId());
        int senderId = me.getId();
        String messageContent = messageTextArea.getText();
        Message message = new Message(senderId, messageContent);
        sendMessage(friendId, message);
        /* End Shimaa */
    }
    // End Nehal Adel
}

