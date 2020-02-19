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

    private static Map<Integer, IClientService> onlineUsers = new HashMap<>();
    IUserDao userDao;
    private User user;

    /*public ChatService() throws RemoteException {
        try {
            userDao = new UserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public ChatService(User user) throws RemoteException {
        try {
            userDao = new UserDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.user = user;
        System.out.println(user.getName());
    }

    @Override
    public List<User> getFriendList() throws RemoteException {
        return userDao.getFriendList(user);
    }

    @Override
    public void changeStatus(UserStatus userStatus) throws RemoteException {
    }

    @Override
    public void sendMessage(int userId, Message message) throws RemoteException {
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
        if (removedUser == null) { // Check User In Map
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
                        System.out.println("Message from group : " + group.getGroupId());
                        System.out.println("Message : " + groupMessage.getMesssagecontent());
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
            searchList = userdao.getNonContactUsers(user.getPhone());
            searchList = searchList.stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchList;
    }

    @Override
    public boolean sendFriendRequest(int fromUserId, int toUserId) throws RemoteException {
        try {
            UserDao userDao = new UserDao();
            boolean notified = userDao.setNotification(fromUserId, toUserId);
            return notified;
//            if (notified) {
                // Call Receive Notification On Client Side
//            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sendFile(RemoteInputStream inFile, String suffix,int friendId ,Message message) throws RemoteException {
        try {
            InputStream istream = RemoteInputStreamClient.wrap(inFile);
            final File tempFile = File.createTempFile(message.getMesssagecontent(), suffix, new File("E:\\"));
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(istream, out);
                BufferedWriter outWrite = new BufferedWriter(new FileWriter("E:\\ids.txt", true));
                String str=String.valueOf(friendId)+"-"+String.valueOf(message.getUserId());
                outWrite.write("\n");
                outWrite.append(str);
                outWrite.write("\n");
                outWrite.append(tempFile.getName());
                outWrite.close();
                IClientService me = onlineUsers.get(message.getUserId());
                IClientService myFriend = onlineUsers.get(friendId);
                me.recieveFileMessage(message);
                myFriend.recieveFileMessage(message);
                System.out.println("Server " + message.getMesssagecontent());
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with the client");
        }

    }

    @Override
    public void getFile(int friendId,int userId,int clickerId) throws RemoteException {
        String str=String.valueOf(friendId)+"-"+String.valueOf(userId);
        File file = new File("E:\\ids.txt");
        BufferedReader br = null;
        RemoteInputStreamServer istream = null;
        boolean loop=true;
        try {
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null&&loop) {
                if(st.equals(str)) {
                    st = br.readLine();
                    istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(st)));
                    loop=false;
                     /*
                     String fileName = selectedFile.getName();
                     String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                     IClientService clicker = onlineUsers.get(clickerId);
                     clicker.dowloadTheFileMessage(istream ,fileExtension,fileName);*/
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (istream != null) istream.close();
        }
    }

    @Override
    public User getUser() throws RemoteException {
        return user;
    }


    /* ِ start  Abdo */

    /* end Abdo */

    /* start sayed */

    /* end sayed */

    /* start nehal */

    /* end nehal */

    /* start aya */

    /* end aya */

    /* start abeer */

    /* end abeer */

    /* start shimaa */

    /* end shimaa */

}
