package org.asasna.chat.server.services;


import org.asasna.chat.common.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

public interface IChatService extends Remote {

    List<User> getFriendList(User user) throws RemoteException;

    void changeStatus(UserStatus userStatus) throws RemoteException;

    Message sendMessage(int userId, Message message) throws RemoteException;

    List<Notification> getNotifications() throws RemoteException;

    void register(int userId, RemoteRef client) throws RemoteException;

    void unRegister(RemoteRef client) throws RemoteException;

    void addFriend() throws RemoteException;

    void sendGroupMsg(Group group, Message groupMessage) throws RemoteException;
}
