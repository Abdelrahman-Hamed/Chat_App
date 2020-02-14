package org.asasna.chat.server.services;

import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

public class ChatService implements IChatService {
    @Override
    public List<User> getFriendList(User user) throws RemoteException {
        return null;
    }

    @Override
    public void changeStatus(UserStatus userStatus) throws RemoteException {

    }

    @Override
    public Message sendMessage(int userId, Message message) throws RemoteException {
        return null;
    }

    @Override
    public List<Notification> getNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void register(int userId, RemoteRef client) throws RemoteException {

    }

    @Override
    public void unRegister(RemoteRef client) throws RemoteException {

    }

    @Override
    public void addFriend(int friendId) throws RemoteException {

    }

    @Override
    public void sendGroupMsg(Group group, Message groupMessage) throws RemoteException {

    }
}
