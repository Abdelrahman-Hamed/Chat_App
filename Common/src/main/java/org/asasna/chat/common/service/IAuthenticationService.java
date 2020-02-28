package org.asasna.chat.common.service;

import javafx.util.Pair;
import org.asasna.chat.common.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthenticationService extends Remote {
    Pair< String ,IChatService> login(String phoneNumber, String password) throws RemoteException;

    void addUser(User me) throws RemoteException;

    boolean isValid(User me) throws RemoteException;
     void signOut(int userID)throws RemoteException;
     //////////////////////////////////////////////////////////////////////////////////////////////////keep me
     int getUserToSave()throws RemoteException;
}
