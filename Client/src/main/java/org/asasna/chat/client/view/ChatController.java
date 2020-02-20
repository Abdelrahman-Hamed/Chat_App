package org.asasna.chat.client.view;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.model.*;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.client.model.SearchedContact;
import org.asasna.chat.common.model.*;

import org.asasna.chat.common.service.IClientService;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatController implements Initializable, IChatController {

    Client client;
    @FXML
    TextField searchTextField;
    @FXML
    BorderPane mainWindow;
    @FXML
    TextArea messageTextArea;
    @FXML
    FontIcon friendList, groupIcon, logoutIcon, friendRequest, notificationIcon, saveChatIcon;
    @FXML
    AnchorPane root;
    @FXML
    VBox contactsList;
    @FXML
    Pane sidePanel;
    @FXML
    VBox contactsView;
    @FXML
    GridPane chatArea;
    @FXML
    private HBox searchArea;
    @FXML
    private HBox chatArea_hbox;
    @FXML
    ScrollPane chatArea_scroll;
    @FXML
    VBox view;
    @FXML
    Circle userImage;
    MSGview viewTextMessage;
    private User me;

    public Contact activeContact;
    private User user;

    public enum Active {
        Profile, Friends, Group, friendRequets
    }

    private Active active;
    private List<User> friends;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setToolTip();
        user = new User(4, "Ahmed", "01027420575");
        Message message = new Message(3, "Hello");
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(new User(1, "Khaled", "014587"));
        list.add(new User(5, "Sayed", "54663"));
        ChatGroup chatGroup = new ChatGroup(1, list.stream().map(u -> u.getId()).collect(Collectors.toList()), "Group1");

//        try {
//            client.sendGroupMessage(chatGroup, message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
        try {
            me = client.getUser();
            System.out.println(me.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            IClientService registeredUser = new Client(this);
            client.registerUser(me.getId(), registeredUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        /*Contact contact = new Contact("Abdelrahman", new Image(getClass().getResource("abdo.jpg").toExternalForm()), UserStatus.ONLINE);
        contactsList.getChildren().add(contact);
        SearchedContact searchedContact = new SearchedContact("Sayed Nabil", new Image(getClass().getResource("abdo.jpg").toExternalForm()), UserStatus.ONLINE);
        contactsList.getChildren().add(searchedContact);*/
        userImage.setFill(new ImagePattern(me.getImage()));
        new Thread(() -> {
            while (root.getScene() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            groupIcon.setOnMouseClicked((e) -> {
                active = Active.Group;
                groupIcon.setIconColor(Color.BLACK);
                searchArea.setStyle("-fx-padding: 20 0 0 0");
                searchArea.setSpacing(20);
                JFXButton create = new JFXButton("+");
                //create.setStyle("");
                create.getStyleClass().clear();
                create.getStyleClass().add("group-create-btn");
                this.searchArea.getChildren().add(create);

            });
            friendList.setOnMouseClicked(e -> {
                active = Active.Friends;
            });
            friendRequest.setOnMouseClicked(e -> {
                active = Active.friendRequets;
            });
            this.root.prefHeightProperty().bind(root.getScene().heightProperty());
            this.root.prefWidthProperty().bind(root.getScene().widthProperty());
            this.messageTextArea.prefHeightProperty().bind(root.getScene().heightProperty());

            this.sidePanel.prefHeightProperty().bind(root.getScene().heightProperty());
            this.mainWindow.prefHeightProperty().bind(root.getScene().heightProperty());
            this.contactsView.prefHeightProperty().bind(root.getScene().heightProperty());
            this.chatArea.prefHeightProperty().bind(root.getScene().heightProperty());
            this.chatArea.prefWidthProperty().bind(root.getScene().widthProperty());
            this.chatArea_hbox.prefWidthProperty().bind(root.getScene().widthProperty());
            //this.chatArea_hbox.prefHeightProperty().bind(root.getScene().heightProperty());
            this.chatArea_scroll.prefWidthProperty().bind(root.getScene().widthProperty().multiply(.5));
            this.chatArea_scroll.prefHeightProperty().bind(root.getScene().heightProperty());
            this.view.prefHeightProperty().bind(root.getScene().heightProperty());
            this.view.prefWidthProperty().bind(root.getScene().widthProperty().multiply(.5));
            this.messageTextArea.prefHeightProperty().bind(root.getScene().heightProperty());
            this.messageTextArea.prefWidthProperty().bind(root.getScene().widthProperty().multiply(.66).subtract(120));

        }).start();
        new Thread(() -> {
            try {
                friends = client.getFriendList();
                friends.forEach(u -> {
                    Contact contact1 = new Contact(u);
                    contact1.setOnMouseClicked(e -> {
                        activeContact = contact1;
                    });
                    Platform.runLater(() -> {
                        contactsList.getChildren().add(contact1);
                    });

                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
        searchTextField.setOnKeyReleased(this::searchContacts);
        SearchedGroupContact searchedGroupContact = new SearchedGroupContact(user);
        contactsList.getChildren().add(searchedGroupContact);
    }

    public void sendAudio() {

    }

    public void saveChat() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(null);
        if(file != null){
            savedFilePath = file.getAbsolutePath();
        }
        saveXmlFile(receiverMessages.get(activeContact.getUser().getId()));
    }

    public void updateProfile() {

    }

    public void addContact() {

    }

    public void createGroupChat() {

    }

    public void changeStatus() {

    }

    public void showNotifications() {

    }

    public void enableChatBot() {

    }

    public void logout() {

    }

    public void exit() {

    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void getSelectedContact() {
        ObservableList<Node> contacts;
        contacts = contactsList.getChildren();
        for (Node c : contacts) {
            c.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                this.activeContact = (Contact) c;
                System.out.println("active Contact is : "+ this.activeContact.getUser().getName());
            });
        }
    }

    private void setToolTip() {
        Tooltip profileTooltip, groupTooltip, addFriendTooltip, notificationTooltip, saveChatTooltip, logoutTooltip;
        profileTooltip = new Tooltip("Profile");
        groupTooltip = new Tooltip("Group Chat");
        addFriendTooltip = new Tooltip("Add Friend");
        notificationTooltip = new Tooltip("Notification");
        saveChatTooltip = new Tooltip("Save Chat");
        logoutTooltip = new Tooltip("Logout");

        profileTooltip.setStyle("-fx-font-size: 14");
        groupTooltip.setStyle("-fx-font-size: 14");
        addFriendTooltip.setStyle("-fx-font-size: 14");
        notificationTooltip.setStyle("-fx-font-size: 14");
        saveChatTooltip.setStyle("-fx-font-size: 14");
        logoutTooltip.setStyle("-fx-font-size: 14");

        Tooltip.install(friendList, profileTooltip);
        Tooltip.install(groupIcon, groupTooltip);
        Tooltip.install(friendRequest, addFriendTooltip);
        Tooltip.install(notificationIcon, notificationTooltip);
        Tooltip.install(saveChatIcon, saveChatTooltip);
        Tooltip.install(logoutIcon, logoutTooltip);

    }

    @Override
    public void displayMessage(Message msg) {
        viewTextMessage = new MSGview(msg);
        if (me.getId() == msg.getUserId()) {
            viewTextMessage.setTextMSGview(SpeechDirection.RIGHT);
            HBox drawer = new HBox();
            Image im = activeContact.getImage();
            Circle circle = new Circle();
            circle.setRadius(20);
            circle.setFill(new ImagePattern(im));
            circle.setCenterY(75);
            view.getChildren().add(circle);
            view.getChildren().add(viewTextMessage);
        } else {
            viewTextMessage.setTextMSGview(SpeechDirection.LEFT);
            Image im = activeContact.getImage();
            Circle circle = new Circle();
            circle.setRadius(20);
            if (im != null)
                circle.setFill(new ImagePattern(im));
            circle.setCenterY(75);
            view.getChildren().add(circle);
            view.getChildren().add(viewTextMessage);
        }
    }

    @Override
    public void changeStatus(UserStatus status) {

    }

    @Override
    public void recieveNotification(Notification notification) {

    }


    //    Start Elsayed Nabil

    public void searchContacts(KeyEvent keyEvent) {
        String searchedMessage = searchTextField.getText();


        if (active == Active.friendRequets) {
            Map<Boolean, List<User>> map = client.search(searchedMessage);
            contactsList.getChildren().clear();
            map.get(true).forEach(user -> {
                contactsList.getChildren().add(new SearchedContact(client, user, true));
            });
            map.get(false).forEach(user -> {
                contactsList.getChildren().add(new SearchedContact(client, user, false));
            });
        } else if (active == Active.Group) {

            contactsList.getChildren().clear();
            friends.stream().filter(f -> f.getPhone().contains(searchTextField.getText()) && f.getStatus() == UserStatus.ONLINE).forEach(f -> {
                contactsList.getChildren().add(new SearchedGroupContact(f));
            });
        } else if (active == Active.Friends) {
            contactsList.getChildren().clear();
            friends.stream()
                    .filter(f -> f.getPhone().contains(searchTextField.getText()) && f.getStatus() == UserStatus.ONLINE)
                    .forEach(f -> {
                        Contact contact = new Contact(f);
                        contactsList.getChildren().add(contact);
                    });

        }
    }
    // End Elsayed Nabil


    //    Start Abdo
    public void recieveGroupMessage(ChatGroup group, Message message) {
        System.out.println("Message from group : ");
        System.out.println(message.getMesssagecontent());
        System.out.println("Group : " + group.getName());
        Platform.runLater(() -> {
            Contact contact = new Contact(group.getName(), null, UserStatus.ONLINE);
            contact.setOnMouseClicked((e) -> {
                this.activeContact = contact;
                System.out.println(activeContact.getName());
            });
            contactsList.getChildren().add(contact);
        });
    }

    public void sendGroupMessage(ChatGroup group, Message message) throws RemoteException {
        client.sendGroupMessage(group, message);

    }
    // End Abdo


    //    Start Aya
    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(null);
        //validation
        if (selectedFile != null) {
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            new Thread(() -> {
                try {
                    int friendId = activeContact.getUser().getId();
                    int senderId = me.getId();
                    Message message = new Message(senderId,fileName);
                    client.sendFileToServer(selectedFile.getPath(), fileExtension,friendId, message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    // End Aya


    //    Start Shimaa

    String savedFilePath = null;
    private Map<Integer, List<Message>> receiverMessages = new HashMap<>();

    @Override
    public void sendMessage(int receiverId, Message message) {
        try {
            client.sendMessage(receiverId, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tempDisplayMessage(Message message) {
        viewTextMessage = new MSGview(message);
        if (me.getId() == message.getUserId()) {
            viewTextMessage.setTextMSGview(SpeechDirection.RIGHT);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    view.getChildren().add(viewTextMessage);
                }
            });
        } else {
            viewTextMessage.setTextMSGview(SpeechDirection.LEFT);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    view.getChildren().add(viewTextMessage);
                }
            });
        }
        saveReceiverMessages(message.getUserId(), message);
    }

    private void saveReceiverMessages(int receiverId, Message message) {
        if (receiverMessages.get(receiverId) == null) {
            List<Message> newMessagesList = new ArrayList<>();
            newMessagesList.add(message);
            receiverMessages.put(receiverId, newMessagesList);
        } else {
             receiverMessages.get(receiverId).add(message);
        }
    }

    public void saveXmlFile(List<Message> list) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = documentBuilderFactory.newDocumentBuilder();
            Document document = build.newDocument();

            Element root = document.createElement("ChatMessage");
            document.appendChild(root);

            for (Message message : list) {
                Element messageNode = document.createElement("Message");
                Element id = document.createElement("Id");
                Element content = document.createElement("Content");

                id.appendChild(document.createTextNode(String.valueOf(message.getUserId())));
                messageNode.appendChild(id);

                content.appendChild(document.createTextNode(String.valueOf(message.getMesssagecontent())));
                messageNode.appendChild(content);
                root.appendChild(messageNode);
            }
            // Save the document to the disk file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            try {
                // location and name of XML file you can change as per need
                FileWriter fileWriter = new FileWriter(savedFilePath);
                StreamResult result = new StreamResult(fileWriter);
                transformer.transform(source, result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (TransformerException ex) {
            System.out.println("Error outputting document");
        } catch (ParserConfigurationException ex) {
            System.out.println("Error building document");
        }
    }

    // End shimaa


    //    Start Abeer Emad
    // End Abeer Emad


    //    Start Nehal Adel

    @FXML
    public void send() throws RemoteException {
        int receiverId = activeContact.getUser().getId();
        int senderId = me.getId();
        String messageContent = messageTextArea.getText();
        messageTextArea.setText("");
        Message message = new Message(senderId, messageContent);
        sendMessage(receiverId, message);
        saveReceiverMessages(receiverId, message);
    }
    // End Nehal Adel
}

