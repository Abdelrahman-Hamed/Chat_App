package org.asasna.chat.common.service;

import com.healthmarketscience.rmiio.RemoteInputStream;
import org.asasna.chat.common.model.*;

import java.io.FileInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClientService extends Remote {
    void recieveMessage(Message message,int receiverId)throws RemoteException;

    void changeStatus(int id, UserStatus status) throws RemoteException;

    void recieveNotivication(Notification notification) throws RemoteException;

    void recieveGroupMessage(ChatGroup group, Message message) throws RemoteException;

    void sendMessage(int receiverId, Message message) throws RemoteException;

    User getUser() throws RemoteException;

    void registerUser(int userId, IClientService client) throws RemoteException;

    void acceptRequest(int fromUserId) throws RemoteException;

    List<Notification> loadNotifications() throws RemoteException;

    /* ِ start  Abdo */

    /* end Abdo */

    /* start sayed */

    void addFriend(User me) throws RemoteException;
    /* end sayed */

    /* start nehal */
    void closeIt() throws RemoteException;
    /* end nehal */

    /* start aya */
    void sendFileToServer(String filePath, String extension, int senderId, Message message) throws RemoteException;

    void recieveFileMessage(Message message) throws RemoteException;

    public void downloadFile(RemoteInputStream inFile, String suffix, String direcotryPath, String name) throws RemoteException;

    void getFile(String directoryPath, String fileName, int senderId) throws RemoteException;

    public void reciveUpateNotification(User updatedUser) throws RemoteException;

    public void changeStatus(User me, UserStatus status) throws RemoteException;
    void signOut(int id) throws RemoteException;

    /* end aya */

    /* start abeer */

    /* end abeer */

    /* start shimaa */
    User getUser(int id) throws RemoteException;
    void receiveAnnouncementFromAdmin(Message message) throws RemoteException;

    void removeFriendFromList(int id) throws RemoteException;

    void removeNotification(int fromUserId) throws RemoteException;

    long getUniqueGroupId() throws RemoteException;

    /* end shimaa */

}
