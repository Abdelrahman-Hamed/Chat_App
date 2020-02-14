package org.asasna.chat.common.service;

import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.UserStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientService extends Remote {
    void recieveMessage(Message message) throws RemoteException;

    void changeStatus(int id, UserStatus status) throws RemoteException;

    void recieveNotivication(Notification notification) throws RemoteException;
}