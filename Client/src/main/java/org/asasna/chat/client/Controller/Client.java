package org.asasna.chat.client.Controller;

import javafx.stage.DirectoryChooser;
import javafx.util.Pair;
import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.client.view.RegisterController;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.common.service.IAuthenticationService;
import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.common.service.IClientService;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.healthmarketscience.rmiio.*;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class Client extends UnicastRemoteObject implements IClientService {
    IChatController chatController;
    RegisterController registerController;
   public IChatService chatService;
    IAuthenticationService authenticationService;
    private User user;

    public Client() throws RemoteException {
    }

    public Client(RegisterController registerController) throws RemoteException {
        try {
            this.registerController = registerController;
            System.setProperty("javax.net.ssl.keyStore", "/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmsim.ks");
            System.setProperty("javax.net.ssl.keyStorePassword", "123456");
            System.setProperty("javax.net.ssl.trustStore", "/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmtruststore.ks");
            System.setProperty("javax.net.ssl.trustStorePassword", "123456");

//            Registry reg = LocateRegistry.getRegistry("10.145.4.235", 5001, new SslRMIClientSocketFactory());
            Registry reg = LocateRegistry.getRegistry(5001);
            authenticationService = (IAuthenticationService) reg.lookup("AuthenticationService");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public Client(IChatController chatController) throws RemoteException {
        this.chatController = chatController;
        Registry reg = null;
//        try {
        /*System.setProperty("javax.net.ssl.keyStore", "/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmsim.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        System.setProperty("javax.net.ssl.trustStore", "/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmtruststore.ks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");*/

        //reg = LocateRegistry.getRegistry("127.0.0.1", 5001, new SslRMIClientSocketFactory());
        reg=LocateRegistry.getRegistry(5001);
//            this.user = new User(4, "Mohamed", "01027420575");
//            chatService.register(this.user.getId(), this);
//        } catch (RemoteException | NotBoundException e) {
        try {
            authenticationService = (IAuthenticationService) reg.lookup("AuthenticationService");
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void recieveMessage(Message message,int receiverId) throws RemoteException {
        chatController.tempDisplayMessage(message,receiverId);
    }

    @Override
    public void changeStatus(int id, UserStatus status) throws RemoteException {

    }


    @Override
    public void recieveNotivication(Notification notification) throws RemoteException {
        chatController.addNotification(notification);
    }

    @Override
    public void recieveGroupMessage(ChatGroup group, Message message) throws RemoteException {
        chatController.recieveGroupMessage(group, message);
    }

    public Map<Boolean, List<User>> search(String phoneNumber) {
        try {

            return chatService.search(phoneNumber);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean sendFriendRequest(int toUserId) {
        try {
            System.out.println("Send Friend Request");
            boolean done = chatService.sendFriendRequest(chatService.getUser().getId(), toUserId);
            return done;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendGroupMessage(ChatGroup group, Message message) throws RemoteException {
        chatService.sendGroupMsg(group, message);
    }

    public Pair< String ,IChatService> login(String phoneNumber, String password) {
        try {
            Pair < String ,IChatService> temp=authenticationService.login(phoneNumber, password);
            chatService = temp.getValue();
            return temp;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(int receiverId, Message message) throws RemoteException {
        chatService.sendMessage(receiverId, message);
    }

    public List<User> getFriendList() throws RemoteException {
        return this.chatService.getFriendList();
    }

    @Override
    public User getUser() throws RemoteException {
        this.user = chatService.getUser();
        return user;
    }

    @Override
    public void registerUser(int userId, IClientService client) throws RemoteException {
        chatService.register(userId, client);
        System.out.println("Resister at client");
    }

    @Override
    public void acceptRequest(int fromUserId) {
        try {
            chatService.acceptRequest(fromUserId, user.getId());
            chatController.removeNotification(fromUserId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Notification> loadNotifications() {
        try {
            return chatService.loadNotifications(user.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean rejectFriendRequest(int userId) {
        try {
            boolean done = chatService.cancelFriendRequest(userId, chatService.getUser().getId());
            chatController.removeNotification(userId);
            return done;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean cancelFriendRequest(int userId) {
        try {
            System.out.println("UserId: " + userId);
            boolean done = chatService.cancelFriendRequest(chatService.getUser().getId(), userId);
            return done;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ِ start  Abdo */

    /* end Abdo */

    /* start sayed */
    @Override
    public boolean sendRecord(int receiverId, int senderId, byte[] buf) throws RemoteException {
        return chatService.sendRecord(receiverId, senderId, buf);
    }

    @Override
    public void recieveRecord(int senderId, byte[] buf) throws RemoteException {
        chatController.recieveRecord(senderId, buf);
    }


    public boolean removeFriend(int friendId) throws RemoteException {
        return chatService.removeFriend(friendId);
    }

    @Override
    public void removeFriendFromList(int id) throws RemoteException {
        System.out.println("Remove Friend From List Client");
        chatController.removeFriendFromList(id);
    }

    public void addFriend(User user){
        chatController.addContact(user);
    }
    /* end sayed */

    /* start nehal */
    public void addUser(User me) throws RemoteException {
        authenticationService.addUser(me);
    }

    public boolean isvalidUser(User me) throws RemoteException {
        return authenticationService.isValid(me);
    }
    public void sendGroupFileToServer(String filePath, String extension, ChatGroup chatGroup, Message message){
        RemoteInputStreamServer istream = null;
        try {
            istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            chatService.sendGroupFile(istream.export(), extension, chatGroup, message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) istream.close();
        }
    }
    /* end nehal */

    /* start aya */
    //List<User> myFriends=getFriendList();
    @Override
    public void sendFileToServer(String filePath, String extension, int senderId, Message message) throws RemoteException {
        RemoteInputStreamServer istream = null;
        try {
            istream = new GZIPRemoteInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            System.out.println("Sending File");
            chatService.sendFile(istream.export(), extension, senderId, message);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (istream != null) istream.close();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////keep me not in the interface
    public int getUserId() throws RemoteException {
        return authenticationService.getUserToSave();
    }
/////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void downloadFile(RemoteInputStream inFile, String suffix, String direcotryPath, String name) throws RemoteException {
        // new Thread(() -> {
        try {
            InputStream istream = RemoteInputStreamClient.wrap(inFile);
            System.out.println(System.getProperty("user.name"));
            final File tempFile = File.createTempFile(name, suffix, new File(direcotryPath));
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(istream, out);
                System.out.println("download file Client");
            }
        } catch (IOException e) {
            // System.out.println("Something went wrong with the client");
            e.printStackTrace();
        }
        //  }).start();

    }

    @Override
    public void recieveFileMessage(Message message) throws RemoteException {//reciver ID !
        //  chatController.tempDisplayMessage(message);
        System.out.println("Recieve File Message");
        chatController.tempDisplayMessage(message);
    }
    @Override
    public void getFile(String directoryPath, String fileName,int senderId)throws RemoteException{
        chatService.getFile(directoryPath, fileName,senderId);
    }

    public List<Integer> setMyFriendsIds(List<User> myFriends) {
        List<Integer> myFriendsIds = null;
        for (int i = 0; i < myFriends.size(); i++) {
            myFriendsIds.add(myFriends.get(i).getId());
        }
        return myFriendsIds;
    }

    /*public List<User> getMyFriendList(int id) throws RemoteException {

        return chatService.getMyFriendList(id);
    }*/
    @Override
    public void changeStatus(User me, UserStatus status) throws RemoteException {
        System.out.println("client");
        // List<User> myFriends=getMyFriendList(id);
        //List<Integer> myFriendsIds=setMyFriendsIds(myFriends);
        chatService.changeUserStatus(me.getId(), status);
        chatService.notifyMyfriends(me.getId());

    }

    @Override
    public void reciveUpateNotification(User updatedUser) throws RemoteException {

        System.out.println("Recived");
        chatController.updateMyContactList(updatedUser);
        System.out.println("Recived2");
    }
    public void signOut(int id) throws RemoteException {
        authenticationService.signOut(id);
    }

    /* end aya */

    /* start abeer */

    /* end abeer */

    /* start shimaa */

    @Override
    public User getUser(int id) throws RemoteException {
        User user2 = chatService.getUser(id);
        return user2;
    }

    @Override
    public void receiveAnnouncementFromAdmin(Message message) throws RemoteException {
        System.out.println("At Client -----> " + message);
        chatController.tempDisplayMessage(message);
    }

    /* end shimaa */
}
