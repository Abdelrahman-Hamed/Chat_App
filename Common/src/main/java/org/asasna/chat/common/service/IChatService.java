package org.asasna.chat.common.service;

import org.asasna.chat.common.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;
import java.util.Map;

import com.healthmarketscience.rmiio.*;

public interface IChatService extends Remote {

    List<User> getFriendList() throws RemoteException;

    void changeStatus(UserStatus userStatus) throws RemoteException;

    void sendMessage(int userId, Message message) throws RemoteException;

    List<Notification> getNotifications() throws RemoteException;

    void register(int userId, IClientService client) throws RemoteException;

    void unRegister(IClientService client) throws RemoteException;

    void addFriend(int friendId) throws RemoteException;

    void sendGroupMsg(ChatGroup group, Message groupMessage) throws RemoteException;

    Map<Boolean, List<User>> search(String phoneNumber) throws RemoteException;

    boolean sendFriendRequest(int fromUserId, int toUserId) throws RemoteException;

    boolean cancelFriendRequest(int fromUserId, int toUserId) throws RemoteException;

    User getUser() throws RemoteException;

    void sendFile(RemoteInputStream inFile, String suffix, int friendId, Message message) throws RemoteException;

    void getFile(String fileName,int clickerId) throws RemoteException;

    void acceptRequest(int fromUserId, int id) throws RemoteException;

    void cancelRequest(int fromUserId, int id) throws RemoteException;

    List<Notification> loadNotifications(int id) throws RemoteException;

    /* ِ start  Abdo */

    /* end Abdo */

    /* start sayed */

    /* end sayed */

    /* start nehal */

    /* end nehal */

    /* start aya */

    /* end aya */

    /* start abeer */

    /* end abeer */

    /* start shimaa */
    public User getUser(int id) throws RemoteException;
    /* end shimaa */
}
