package org.asasna.chat.client.model;

import javafx.scene.layout.HBox;
import org.asasna.chat.common.model.*;

public interface IChatController{
    void displayMessage(Message msg);

    void changeStatus(UserStatus status);

    void recieveNotification(Notification notification);

    void recieveGroupMessage(ChatGroup group, Message message);

    void sendMessage(int receiverId, Message message);

    void tempDisplayMessage(Message message);

    void addNotification(Notification notification);
    public void updateMyContactList(User updatedUser);
    public void tempFileDisplayMessage(Message message);
    public void tempDisplayMessage(Message message, int receiverId);



    void removeNotification(int fromUserId);

    void removeFriendFromList(int id);

    void addContact(User user);
    /*nehal*/
    void serverIsDownHandler();
    /*nehal end*/
}
