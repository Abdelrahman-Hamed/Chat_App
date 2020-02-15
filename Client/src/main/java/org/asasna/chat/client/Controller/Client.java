package org.asasna.chat.client.Controller;

import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.common.service.IClientService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client extends UnicastRemoteObject implements IClientService {
    IChatController chatController;
    IChatService chatService;

    protected Client() throws RemoteException {
    }

    public Client(IChatController chatController) throws RemoteException {
        this.chatController = chatController;
    }

    @Override
    public void recieveMessage(Message message) throws RemoteException {
        chatController.displayMessage(message);
    }

    @Override
    public void changeStatus(int id, UserStatus status) throws RemoteException {

    }

    @Override
    public void recieveNotivication(Notification notification) throws RemoteException {

    }

    @Override
    public List<User> search(String phoneNumber) {
        System.out.println(phoneNumber);
        return null;
    }
}
