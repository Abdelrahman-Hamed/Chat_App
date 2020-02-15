package org.asasna.chat.server.services;

import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.model.dao.IUserDao;
import org.asasna.chat.server.model.dao.UserDao;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatService extends UnicastRemoteObject implements IChatService {

    private static Map<Integer,List<Message>> receiverMessages = new HashMap<>();
    private static Map<Integer, IChatService> onlineUsers = new HashMap<>();
    IUserDao userDao;
    private User user;

    public ChatService() throws RemoteException {
        try {
            userDao = new UserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getFriendList(User user) throws RemoteException {
        return userDao.getFriendList(user);
    }

    @Override
    public void changeStatus(UserStatus userStatus) throws RemoteException {

    }

    @Override
    public Message sendMessage(int userId, Message message) throws RemoteException {
        return null;
    }

    @Override
    public List<Notification> getNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void register(int userId, RemoteRef client) throws RemoteException {

    }

    @Override
    public void unRegister(RemoteRef client) throws RemoteException {

    }

    @Override
    public void addFriend(int friendId) throws RemoteException {

    }

    @Override
    public void sendGroupMsg(Group group, Message groupMessage) throws RemoteException {

    }

    @Override
    public List<User> search(String phoneNumber) throws RemoteException {
        List<User> searchList =new ArrayList<User>();
        UserDao userdao= null;
        try {
            userdao = new UserDao();
            searchList= userdao.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        searchList.stream().filter(x -> phoneNumber.matches(x.getPhone()+"[0-9]*")).collect(Collectors.toList());
        return  searchList;
    }

    @Override
    public void sendFriendRequest(String fromPhoneNumber, String toPhoneNumber) {

    }
}
