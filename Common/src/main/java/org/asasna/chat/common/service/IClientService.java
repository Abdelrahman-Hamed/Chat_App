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
    public void sendFileToServer(String filePath,String extension) throws RemoteException;

}
