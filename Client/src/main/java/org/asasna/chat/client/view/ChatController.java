package org.asasna.chat.client.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import org.asasna.chat.client.model.Contact;
import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.client.model.MSGview;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable, IChatController {

    @FXML
    TextArea messageTextArea;
    @FXML
    FontIcon profileIcon, groupIcon, logoutIcon, addFriendIcon, notificationIcon, saveChatIcon;

    @FXML
    VBox contactsList;

    MSGview viewTextMessage;
    private User me;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setToolTip();
        me = new User();
//        Contact contact = new Contact("Abdelrahman", new Image(getClass().getResourceAsStream("../resources/org/asasna/chat/client/abdo.jpg")), UserStatus.ONLINE);
//        contactsList.getChildren().add(contact);
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

    public User getSelectedContact() {
        return null;
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
            viewTextMessage.setTextMSGview("RIGHT");
        }
        else {
            viewTextMessage.setTextMSGview("LEFT");
        }
    }

    @Override
    public void changeStatus(UserStatus status) {

    }

    @Override
    public void recieveNotification(Notification notification) {

    }
}
