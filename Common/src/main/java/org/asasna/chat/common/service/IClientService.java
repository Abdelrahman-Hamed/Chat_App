package org.asasna.chat.common.service;

import org.asasna.chat.common.model.*;

import java.io.FileInputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IClientService extends Remote {
    void recieveMessage(Message message) throws RemoteException;

    void changeStatus(int id, UserStatus status) throws RemoteException;

    void recieveNotivication(Notification notification) throws RemoteException;
    void sendFileToServer(String filePath,String extension) throws RemoteException;

    void recieveGroupMessage(ChatGroup group, Message message) throws RemoteException;

    void sendMessage(int receiverId, Message message) throws RemoteException;

    User getUser() throws RemoteException;

    void setUser(User user) throws RemoteException;

    void registerUser(int userId, IClientService client) throws RemoteException;

}
