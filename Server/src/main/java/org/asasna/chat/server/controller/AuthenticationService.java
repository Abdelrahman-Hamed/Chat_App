package org.asasna.chat.server.controller;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.service.IAuthenticationService;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.model.dao.IUserDao;
import org.asasna.chat.server.model.dao.UserDao;
import org.asasna.chat.server.services.ChatService;
import org.asasna.chat.server.view.PasswordAuthentication;

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
            if(user == null) return null;
            return new ChatService(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    IUserDao userDao ;
    {
        try {
            userDao = new UserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    User user = new User();
    @Override
    public void addUser(User me) throws RemoteException {

        try {
            userDao = new UserDao();
            userDao.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        if(userDao.getUser(user.getPhone()) == null) {
//            //PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
//            //user.setPassword(passwordAuthentication.hash(user.getPassword()));
//            userDao.addUser(user);
//        }
    }
    public boolean isValid(User me) throws RemoteException{
        return ( userDao.getUser(me.getPhone()) == null );
    }

    public ObservableList<PieChart.Data> getGenderData() throws RemoteException, SQLException {
        userDao = new UserDao();
        return  userDao.getUsersByGender();
    }
    public ObservableList<PieChart.Data> getStatusData() throws RemoteException, SQLException {
        userDao = new UserDao();
        return  userDao.getUsersByStatus();
    }

}
