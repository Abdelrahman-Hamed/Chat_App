package org.asasna.chat.client.model;

import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.UserStatus;

import java.rmi.RemoteException;

public interface IChatController {
    void displayMessage(Message msg);

    void changeStatus(UserStatus status);

    void recieveNotification(Notification notification);
}
