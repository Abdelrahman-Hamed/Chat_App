package org.asasna.chat.client.Controller;

import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.common.service.IClientService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.healthmarketscience.rmiio.*;

public class Client extends UnicastRemoteObject implements IClientService {
    IChatController chatController;
    IChatService chatService;
    private User user;

    protected Client() throws RemoteException {
    }

    public Client(IChatController chatController) throws RemoteException {
        this.chatController = chatController;
        try {
            Registry reg = LocateRegistry.getRegistry(5000);
            chatService = (IChatService) reg.lookup("ChatService");
            this.user = new User(4, "Mohamed", "01027420575");
            chatService.register(this.user.getId(), this);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recieveMessage(Message message) throws RemoteException {

    }

    @Override
    public void changeStatus(int id, UserStatus status) throws RemoteException {

    }

    @Override
    public void sendFileToServer(String filePath, String extension) throws RemoteException {
        RemoteInputStreamServer istream = null;
        try {
            istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            chatService.sendFile(istream.export(), extension);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) istream.close();
        }
    }

    @Override
    public void recieveNotivication(Notification notification) throws RemoteException {

    }

    @Override
    public void recieveGroupMessage(ChatGroup group, Message message) throws RemoteException {
        chatController.recieveGroupMessage(group, message);
    }

    public List<User> search(String phoneNumber) {
        try {
            return chatService.search(phoneNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    public void sendFriendRequest(String toUserPhone) {
        try {
            chatService.sendFriendRequest("01279425232", toUserPhone);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendGroupMessage(ChatGroup group, Message message) throws RemoteException {
        chatService.sendGroupMsg(group, message);
    }
}
