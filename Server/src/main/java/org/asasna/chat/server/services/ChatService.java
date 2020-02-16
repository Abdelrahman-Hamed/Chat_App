package org.asasna.chat.server.services;

import com.mysql.cj.xdevapi.Client;
import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.common.service.IClientService;
import org.asasna.chat.server.model.dao.IUserDao;
import org.asasna.chat.server.model.dao.UserDao;

import java.io.*;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.healthmarketscience.rmiio.*;
import org.apache.commons.io.IOUtils;

public class ChatService extends UnicastRemoteObject implements IChatService {

    private Map<Integer, List<Message>> receiverMessages = new HashMap<>();
    private static Map<Integer, IClientService> onlineUsers = new HashMap<>();
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
    public void sendMessage(int userId, Message message) throws RemoteException {
        saveReceiverMessages(userId, message);
        IClientService me = onlineUsers.get(message.getUserId());
        IClientService myFriend = onlineUsers.get(userId);
        me.recieveMessage(message);
        myFriend.recieveMessage(message);
    }

    @Override
    public List<Notification> getNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void register(int userId, IClientService client) throws RemoteException {
        onlineUsers.put(userId, client);
        System.out.println(onlineUsers);
    }

    @Override
    public void unRegister(IClientService client) throws RemoteException {
        IClientService removedUser = onlineUsers.remove(client.getUser().getId());
        if(removedUser == null){ // Check User In Map
            System.out.println("Not Founed To Remove");
        }
    }

    @Override
    public void addFriend(int friendId) throws RemoteException {

    }

    @Override
    public void sendGroupMsg(ChatGroup group, Message groupMessage) throws RemoteException {
        System.out.println(group.getParticipents());
        onlineUsers.keySet()
                .parallelStream()
                .filter(u -> group.getParticipents().contains(u))
                .map(u -> onlineUsers.get(u))
                .forEach(u -> {
                    try {
                        u.recieveGroupMessage(group, groupMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public List<User> search(String phoneNumber) throws RemoteException {
        List<User> searchList = new ArrayList<User>();
        UserDao userdao = null;
        try {
            userdao = new UserDao();
            searchList= userdao.getAllUsers();

            searchList = searchList.stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList());
            System.out.println("Size1: " + searchList.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  searchList;
    }

    @Override
    public void sendFriendRequest(String fromPhoneNumber, String toPhoneNumber) throws RemoteException{

    }

    @Override
    public void sendFile(RemoteInputStream inFile, String suffix) throws RemoteException {
        try {
            //,int senderID,int userID
            InputStream istream = RemoteInputStreamClient.wrap(inFile);
            final File tempFile = File.createTempFile("tmp", suffix, new File("C:\\Users\\Aya\\Desktop"));
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(istream, out);
              /*  BufferedWriter outWrite = new BufferedWriter(new FileWriter("C:\\Users\\Aya\\Desktop\\ids.txt", true));
                String str=String.valueOf(senderID+userID);
                outWrite.write(tempFile.getName());
                outWrite.write(str);
                outWrite.close();*/
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with the client");
        }

    }

    @Override
    public void getFile(String filePath) throws RemoteException {
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

    private void saveReceiverMessages(int receiverId, Message message){
        List<Message> messagesList = receiverMessages.get(receiverId);
        if (messagesList.isEmpty()) {
            List<Message> newMessagesList = new ArrayList<>();
            newMessagesList.add(message);
            receiverMessages.put(receiverId, newMessagesList);
        } else {
            receiverMessages.get(receiverId).add(message);
        }
    }

}
