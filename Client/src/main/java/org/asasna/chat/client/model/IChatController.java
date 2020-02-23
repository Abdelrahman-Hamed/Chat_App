package org.asasna.chat.client.model;

import org.asasna.chat.common.model.*;

import java.rmi.RemoteException;

public interface IChatController{
    void displayMessage(Message msg);

    void changeStatus(UserStatus status);

    void recieveNotification(Notification notification);

    void recieveGroupMessage(ChatGroup group, Message message);

    void sendMessage(int receiverId, Message message);

    void tempDisplayMessage(Message message);

    void addNotification(Notification notification);
    public void updateMyContactList(User updatedUser);
}
