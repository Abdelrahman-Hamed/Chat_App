package org.asasna.chat.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthenticationService extends Remote {
    IChatService login(String phoneNumber, String password) throws RemoteException;
}
