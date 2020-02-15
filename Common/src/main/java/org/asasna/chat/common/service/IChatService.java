package org.asasna.chat.common.service;

import org.asasna.chat.common.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

import com.healthmarketscience.rmiio.*;

public interface IChatService extends Remote {

    List<User> getFriendList(User user) throws RemoteException;

    void changeStatus(UserStatus userStatus) throws RemoteException;

    Message sendMessage(int userId, Message message) throws RemoteException;

    List<Notification> getNotifications() throws RemoteException;

    void register(int userId, IClientService client) throws RemoteException;

    void unRegister(RemoteRef client) throws RemoteException;

    void addFriend(int friendId) throws RemoteException;

    void sendGroupMsg(ChatGroup group, Message groupMessage) throws RemoteException;

    List<User> search(String phoneNumber) throws RemoteException;

    void sendFriendRequest(String fromPhoneNumber, String toPhoneNumber) throws RemoteException;

    public void sendFile(RemoteInputStream inFile, String suffix) throws RemoteException;

    public void getFile(String filePath) throws RemoteException;
}
