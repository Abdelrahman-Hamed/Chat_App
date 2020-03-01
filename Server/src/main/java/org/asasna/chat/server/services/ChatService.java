package org.asasna.chat.server.services;


import org.apache.log4j.Logger;
import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.common.service.IClientService;
import org.asasna.chat.server.App;
import org.asasna.chat.server.model.dao.ContactDao;
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
import org.asasna.chat.server.view.PasswordAuthentication;

public class ChatService extends UnicastRemoteObject implements IChatService {

    public static Map<Integer, IClientService> onlineUsers = new HashMap<>(); // will need it
    IUserDao userDao;
    private User user;
    private static long uniqueGroupId = 1;
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
        me.recieveMessage(message,userId);
        myFriend.recieveMessage(message,userId);
    }

    @Override
    public List<Notification> getNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void register(int userId, IClientService client) throws RemoteException {
        onlineUsers.put(userId, client);
        Logger.getLogger(App.class).info("Client Connected : " + client);

        //System.out.println(onlineUsers);
    }

    @Override
    public void unRegister(IClientService client) throws RemoteException {
        IClientService removedUser = onlineUsers.remove(client.getUser().getId());
        if (removedUser == null) { // Check User In Map
            //System.out.println("Not Found To Remove");
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
    public Map<Boolean, List<User>> search(String phoneNumber) throws RemoteException {
        List<User> searchList = new ArrayList<User>();
        Map<Boolean, List<User>> map = new HashMap<>();
        UserDao userdao = null;
        try {
            userdao = new UserDao();
            map = userdao.getNonContactUsers(user.getId());
            map.put(true, map.get(true).stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList()));
            map.put(false, map.get(false).stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList()));
//            searchList = searchList.stream().filter(user -> user.getPhone().contains(phoneNumber)).collect(Collectors.toList());
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public boolean sendFriendRequest(int fromUserId, int toUserId) throws RemoteException {
        try {
            UserDao userDao = new UserDao();
            boolean notified = userDao.setNotification(fromUserId, toUserId);
            if (notified) {
//                 Call Receive Notification On Client Side
                IClientService toClient = onlineUsers.get(toUserId);
                if (toClient != null) {
                    System.out.println("Get In Here");
                    toClient.recieveNotivication(new Notification(NotificationType.FRIEND_REQUEST, userDao.getUser(fromUserId)));
                }
            }
            return notified;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelFriendRequest(int fromUserId, int toUserId) throws RemoteException {
        try {
            UserDao userDao = new UserDao();
            boolean notified = userDao.cancelNotification(fromUserId, toUserId);
            IClientService toUserClient = onlineUsers.get(toUserId);
            System.out.println("chatService 1");
            if(toUserClient != null){
                System.out.println("chatService 2");
                toUserClient.removeNotification(fromUserId);
            }
            return notified;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void acceptRequest(int fromUserId, int id) throws RemoteException {
        sendFriendRequest(id, fromUserId);
    }

    @Override
    public void cancelRequest(int fromUserId, int id) throws RemoteException {
        cancelFriendRequest(id, fromUserId);
    }

    @Override
    public List<Notification> loadNotifications(int id) throws RemoteException {
        try {
            UserDao userDao = new UserDao();
            return userDao.getNotification(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User getUser() throws RemoteException {
        return user;
    }

    /* ِ start  Abdo */

    /* end Abdo */

    /* start sayed */


    @Override
    public void addMeAsFriend(int meId, int newFriend) throws RemoteException {
        IClientService newFriendClient = onlineUsers.get(newFriend);
        if(newFriendClient != null) {
            newFriendClient.addFriend(userDao.getUser(meId));
        }
    }
    private String createFileInServer(RemoteInputStream export, String extension, Message message) {

        try {
            InputStream istream = RemoteInputStreamClient.wrap(export);
            final File tempFile = File.createTempFile(message.getMesssagecontent(), extension, new File("./Server/src/main/resources/org/asasna/chat/server/files"));
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(istream, out);
                return tempFile.getName();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendGroupFile(RemoteInputStream export, String extension, ChatGroup chatGroup, Message message) throws RemoteException {
        String name = createFileInServer(export, extension, message);
        message.setMesssagecontent(name);
        chatGroup.getParticipents().forEach((participentId) -> {
            try {
                IClientService myFriend = onlineUsers.get(participentId);
                if (myFriend != null)
                    myFriend.recieveGroupMessage(chatGroup, message);
                System.out.println("Server " + message.getMesssagecontent());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }



    @Override
    public boolean removeFriend(int friendId) throws RemoteException {
        try {
            ContactDao contactDao = new ContactDao();
            boolean result = contactDao.removeFriend(user.getId(), friendId);
            System.out.println("Get In Remove Friend");
            if (result) {
                IClientService clientService = onlineUsers.get(friendId);
                if (clientService != null)
                    clientService.removeFriendFromList(user.getId());
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /* end sayed */

    /* start nehal */
    public ChatService() throws RemoteException {
        super();
    }

    @Override
    public void sendAnnouncementsToOnlineUsers(Message message) throws RemoteException {
        for (IClientService client : onlineUsers.values()) {
            client.receiveAnnouncementFromAdmin(message);
        }
    }
    public void closeServer()throws RemoteException{
        for (IClientService client : onlineUsers.values()){
            client.closeIt();
        }
    }
    public void unRegisterAll() throws RemoteException{
        onlineUsers.clear();
    }
    /* end nehal */

    /* start aya */
    @Override
    public void sendFile(RemoteInputStream inFile, String suffix, int friendId, Message message) {
        try {
            String name = createFileInServer(inFile, suffix, message);
            message.setMesssagecontent(name);
            IClientService me = onlineUsers.get(message.getUserId());
            IClientService myFriend = onlineUsers.get(friendId);
            message.setMesssagecontent(name);
            me.recieveFileMessage(message);
            myFriend.recieveFileMessage(message);
            System.out.println("Server " + message.getMesssagecontent());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFile(String direcotryPath, String fileName, int clickerId) throws RemoteException {

        RemoteInputStreamServer istream = null;
        try {

            istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream("./Server/src/main/resources/org/asasna/chat/server/files/" + fileName)));///pathhhhhhhhh
            String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            IClientService clicker = onlineUsers.get(clickerId);
            clicker.downloadFile(istream, fileExtension, direcotryPath, fileName);
            System.out.println("ChatService getfile");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) istream.close();
        }
    }

    public void changeUserStatus(int id, UserStatus status) throws RemoteException {
        // System.out.println("chatServer");
        userDao.updateUserStatues(id, status);
    }

    public void notifyMyfriends(int myId) throws RemoteException {
        List<User> myFriends = getMyFriendList(myId);
        System.out.println(" id " + myId);
        System.out.println(" size " + myFriends.size());
        User me = userDao.getUser(myId);
        for (int i = 0; i < myFriends.size(); i++) {
            if (myFriends.get(i).getStatus() != UserStatus.OFFLINE) {
                IClientService myFriend = onlineUsers.get(myFriends.get(i).getId());
                System.out.println(" id  to send to " + myFriends.get(i).getId());
                System.out.println(" looooooooooooooooooooooop  i" + i);
                if (myFriend == null) {
                    System.out.println(" null friend ");
                } else {
                    myFriend.reciveUpateNotification(me);
                    System.out.println(" not null friend ");
                }


            }
        }
    }

    @Override
    public List<User> getMyFriendList(int id) throws RemoteException {
        User friend = new User();
        friend.setId(id);
        return userDao.getFriendList(friend);
    }


    public boolean removeClient(int id) throws RemoteException {
        IClientService returnedValue = (IClientService) onlineUsers.remove(id);
        if (returnedValue != null) {
            return true;
        } else {
            return false;
        }
    }

    /* end aya */

    /* start abeer */
    @Override
    public void UpdateUser(User user) {
        try {
            UserDao userdao = new UserDao();
            PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
//            user.setPassword(passwordAuthentication.hash(user.getPassword()));
            boolean done = userdao.updateUser(user.getId(), user);
            if (!done)
                System.out.println("cant update user ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    /* end abeer */

    /* start shimaa */
    @Override
    public User getUser(int id) throws RemoteException {
        return userDao.getUser(id);
    }

    /* end shimaa */
    @Override
    public long getUniqueGroupId() {
        return uniqueGroupId++;
    }
}
