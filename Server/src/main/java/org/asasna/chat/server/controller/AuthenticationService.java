package org.asasna.chat.server.controller;

import org.asasna.chat.common.model.User;
import org.asasna.chat.common.service.IAuthenticationService;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.model.dao.UserDao;
import org.asasna.chat.server.services.ChatService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class AuthenticationService extends UnicastRemoteObject implements IAuthenticationService{
    public AuthenticationService() throws RemoteException {
    }

    @Override
    public IChatService login(String phoneNumber, String password) throws RemoteException {
        try {
            UserDao userDao = new UserDao();
            User user = userDao.getUser(phoneNumber, password);
            return new ChatService(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}