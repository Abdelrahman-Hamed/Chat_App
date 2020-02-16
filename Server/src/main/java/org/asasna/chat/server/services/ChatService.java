package org.asasna.chat.server.services;

import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.model.dao.IUserDao;
import org.asasna.chat.server.model.dao.UserDao;

import java.io.*;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.healthmarketscience.rmiio.*;
import org.apache.commons.io.IOUtils;

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

    public ChatService(User user) throws RemoteException{
        this.user = user;
    }
    @Override
    public List<User> getFriendList(User user) throws RemoteException {
        return null;
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

            searchList = searchList.stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList());
            System.out.println("Size1: " + searchList.size());
            return  searchList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void sendFriendRequest(String fromPhoneNumber, String toPhoneNumber) throws RemoteException{

    }

   @Override
    public void sendFile(RemoteInputStream inFile,String suffix) throws RemoteException{
        try {
            //,int senderID,int userID
            InputStream istream = RemoteInputStreamClient.wrap(inFile);
            final File tempFile = File.createTempFile("tmp", suffix,new File("C:\\Users\\Aya\\Desktop"));
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(istream, out);
              /*  BufferedWriter outWrite = new BufferedWriter(new FileWriter("C:\\Users\\Aya\\Desktop\\ids.txt", true));
                String str=String.valueOf(senderID+userID);
                outWrite.write(tempFile.getName());
                outWrite.write(str);
                outWrite.close();*/
            }
        } catch (IOException  e) {
            System.out.println("Something went wrong with the client");
        }

    }

    @Override
    public void getFile(String filePath) throws RemoteException{
        RemoteInputStreamServer istream = null;
        try {
            istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            //chatService.sendFile(istream.export(),extension);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) istream.close();
        }
    }



}
