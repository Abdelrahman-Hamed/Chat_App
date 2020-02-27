package org.asasna.chat.client.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.io.FileDeleteStrategy;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.model.*;
import org.asasna.chat.common.model.Message;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.client.model.SearchedContact;
import org.asasna.chat.common.model.*;

import org.asasna.chat.common.service.IClientService;
import org.controlsfx.control.Notifications;

import org.kordamp.ikonli.javafx.FontIcon;

import java.io.*;
import javax.sound.sampled.*;

import org.jcodec.common.model.AudioBuffer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
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
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.FileWriter;
import java.io.File;
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
    @FXML
    JFXButton createbtn;
    @FXML
    Button sendButton;
    @FXML
    Circle receiverImage;
    @FXML
    Label receiverNameLabel;

    @FXML
    Circle status;


    @FXML
    FontIcon microphoneId;
    private long start, end;
    private TargetDataLine line;
    MSGview viewTextMessage;
    private User me;
    private List<Notification> notifications = new ArrayList<>();
    public Contact activeContact;
    private User user;

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public enum Active {
        Profile, Friends, Group, friendRequets, Notifications
    }

    private Active active;
    private List<User> friends;
    ObservableSet<Contact> oContacts = FXCollections.observableSet();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setToolTip();
        /*user = new User(4, "Ahmed", "01027420575");
        Message message = new Message(3, "Hello");
        List<User> list = new ArrayList<>();
        list.add(user);
        //groupContacts = FXCollections.observableArrayList();

        list.add(new User(1, "Khaled", "014587"));
        list.add(new User(5, "Sayed", "54663"));
        ChatGroup chatGroup = new ChatGroup(1, list.stream().map(u -> u.getId()).collect(Collectors.toList()), new Image(getClass().getResource("group.png").toExternalForm()), "Group1");
*/
        /*try {
            client.sendGroupMessage(chatGroup, message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
        try {
            me = client.getUser();

                status.setStyle("-fx-fill:  #33FF4B");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        chatArea_scroll.setContent(view);
        view.heightProperty().addListener((ob, ol, nw) -> {
            chatArea_scroll.setVvalue((double) nw);
        });
        view.prefWidthProperty().bind(chatArea_scroll.widthProperty());
        searchArea.setStyle("-fx-padding: 20 0 0 0");
        searchArea.setSpacing(5);
        createbtn = new JFXButton("+");
        //create.setStyle("");
        createbtn.getStyleClass().clear();
        createbtn.getStyleClass().add("group-create-btn");
        this.searchArea.getChildren().add(createbtn);
        createbtn.setVisible(false);
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
        //userImage.setFill(new ImagePattern(me.getImage()));
        new Thread(() -> {
//            while (root.getScene() == null) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            groupIcon.setOnMouseClicked((e) -> {
                active = Active.Group;
                groupIcon.setIconColor(Color.BLACK);
                createbtn.setVisible(true);
                createbtn.setOnAction(e1 -> {
                    ChatGroup chatGroup1 = new ChatGroup(1, (List<Integer>)
                            contactsList.getChildren().stream()
                                    .filter(c -> ((SearchedGroupContact) c).getSelected())
                                    .map(c -> ((SearchedGroupContact) c).getUser())
                                    .peek(System.out::println)
                                    .mapToInt(u -> u.getId())
                                    .boxed()
                                    .collect(Collectors.toList())
                            , null, "Group1");
                    chatGroup1.getParticipents().add(me.getId());
                    GroupContact groupContact = new GroupContact(chatGroup1);
                    groupContact.setOnMouseClicked(ec -> {
                        activeContact = groupContact;
                    });
                    try {
                        contactsList.getChildren().clear();
                        friends = client.getFriendList();
                        friends.forEach(u -> {
                            Contact contact1 = new Contact(u);
                            contact1.setOnMouseClicked(ev -> {
                                activeContact = contact1;
                            });
                            oContacts.add(contact1);
                            contactsList.getChildren().add(contact1);


                        });
                    } catch (RemoteException er) {
                        er.printStackTrace();
                    }
                    contactsList.getChildren().add(groupContact);
                    oContacts.add(groupContact);
                    createbtn.setVisible(false);
                    active = Active.Friends;
                    Bindings.bindContent(contactsList.getChildren(), FXCollections.observableArrayList(oContacts));
                    //Bindings.bindContentBidirectional(FXCollections.observableArrayList(oContacts), contactsList.getChildren());
                    //oContacts.forEach(System.out::println);
                });
            });

            friendList.setOnMouseClicked(e -> {
                active = Active.Friends;
                oContacts.forEach(System.out::println);
                contactsList.getChildren().clear();
                createbtn.setVisible(false);
                Bindings.bindContent(contactsList.getChildren(), FXCollections.observableArrayList(oContacts));
                //Bindings.bindContentBidirectional(FXCollections.observableArrayList(oContacts), contactsList.getChildren());
            });
            friendRequest.setOnMouseClicked(e -> {
                active = Active.friendRequets;
            });
            notificationIcon.setOnMouseClicked(e -> {
                active = Active.Notifications;
            });
            /*this.root.prefHeightProperty().bind(root.getScene().heightProperty());
            this.root.prefWidthProperty().bind(root.getScene().widthProperty());
            this.messageTextArea.prefHeightProperty().bind(root.getScene().heightProperty());

            this.sidePanel.prefHeightProperty().bind(root.getScene().heightProperty());
            this.mainWindow.prefHeightProperty().bind(root.getScene().heightProperty());
            this.contactsView.prefHeightProperty().bind(root.getScene().heightProperty());
            this.chatArea.prefHeightProperty().bind(root.getScene().heightProperty());
            this.chatArea.prefWidthProperty().bind(root.getScene().widthProperty());
            this.chatArea_scroll.prefWidthProperty().bind(root.getScene().widthProperty().multiply(.5));
            this.chatArea_scroll.prefHeightProperty().bind(root.getScene().heightProperty());
            //this.chatArea_scroll.vvalueProperty().bind(this.view.heightProperty());
            this.chatArea_scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // shimaa
            this.view.prefHeightProperty().bind(this.root.getScene().heightProperty());
            this.view.prefWidthProperty().bind(this.root.getScene().widthProperty().multiply(.5));
            this.messageTextArea.prefHeightProperty().bind(root.getScene().heightProperty());
            this.messageTextArea.prefWidthProperty().bind(root.getScene().widthProperty().multiply(.66).subtract(120));
*/
            this.chatArea_scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // shimaa
            this.chatArea_scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // shimaa

        }).start();
        /*******************************/
        //Shimaa
        setInitialContact();
        setListnerForPressingEnter();
        messageTextArea.setStyle("-fx-font-size:15");
        if (activeContact.getUser().getStatus() == UserStatus.OFFLINE){
            messageTextArea.setDisable(true);
        } else {
            messageTextArea.setDisable(false);
        }
        /*******************************/
        new Thread(() -> {
            try {
                friends = client.getFriendList();
                friends.forEach(u -> {
                    Contact contact1 = new Contact(u);
                    contact1.setOnMouseClicked(e -> {
                        activeContact = contact1;
                    });
                    oContacts.add(contact1);
                    Platform.runLater(() -> {
                        contactsList.getChildren().add(contact1);
                    });

                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            this.notifications = client.loadNotifications();
            System.out.println("Size: " + notifications.size());
        }).start();
        searchTextField.setOnKeyReleased(this::searchContacts);
        /*SearchedGroupContact searchedGroupContact = new SearchedGroupContact(user);
        contactsList.getChildren().add(searchedGroupContact);*/
        sendButton.setOnAction(e -> {
            try {
                send();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
//        SearchedGroupContact searchedGroupContact = new SearchedGroupContact(user);
//        contactsList.getChildren().add(searchedGroupContact);

        setListnerForPressingEnter(); // shimaa
        messageTextArea.setStyle("-fx-font-size:14");
        /*SearchedGroupContact searchedGroupContact = new SearchedGroupContact(user);
        contactsList.getChildren().add(searchedGroupContact);*/


//        Sayed Start
        microphoneId.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new Thread(() -> {
                    microphoneId.setIconLiteral("dashicons-marker");
                    start();
                }).start();
            }
        });
        microphoneId.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new Thread(() -> {
                    stop();
                    microphoneId.setIconLiteral("dashicons-microphone");
                }).start();
            }
        });




//        Sayed End
    }

    //      Sayed Start
    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    private void start() {
        try {

            AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
            float rate = 44100.0f;
            int channels = 2;
            int sampleSize = 16;
            boolean bigEndian = false;
            AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            /*line.open(format);
            line.start();   // start capturing*/
            start = System.currentTimeMillis();
            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            File wavFile = new File("./Client/src/main/resources/org/asasna/chat/client/audio/record.wav");
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
//        SearchedGroupContact searchedGroupContact = new SearchedGroupContact(user);
//        contactsList.getChildren().add(searchedGroupContact);

        setListnerForPressingEnter(); // shimaa
        messageTextArea.setStyle("-fx-font-size:14");
    }
    private void stop(){
        line.stop();
        line.close();
        end = System.currentTimeMillis();
        System.out.println("Finished");
        if (end - start < 1000) {
            System.out.println("Hold To Record, Release To Send");
            removeWavFile();
        } else {
            new Thread(() -> {
                try {

                    byte[] buf = convertFileToBytes();
                    int receiverId = activeContact.getUser().getId();
                    int senderId = me.getId();
                    boolean sent = client.sendRecord(receiverId, senderId, buf);
                    System.out.println(buf.length);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }
    private byte[] convertFileToBytes(){
        byte[] buf = new byte[1024];
        try{
            File wavFile = new File("./Client/src/main/resources/org/asasna/chat/client/audio/record.wav");
            FileInputStream fileInputStream = new FileInputStream(wavFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                System.out.println("read " + readNum + " bytes,");
            }
            removeWavFile();
            buf = bos.toByteArray();
            return buf;
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
            return buf;
        }
    }
    private void removeWavFile(){
        try {
            File wavFile = new File("./Client/src/main/resources/org/asasna/chat/client/audio/record.wav");
            FileDeleteStrategy.FORCE.delete(wavFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    Sayed End

    public void sendAudio() {

    }

    public void saveChat() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("*.xml");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            savedFilePath = file.getAbsolutePath();
            System.out.println(savedFilePath);
            saveXmlFile(receiverMessages.get(activeContact.getUser().getId()));
        } else{
            System.out.println("you didn't save chat");
        }
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
            });
        }
        /*    });
        }
        System.out.println("active Contact is : " + this.activeContact.getUser().getName());
        /************************************************************************************/
        // Shimaa
        receiverImage.setFill(new ImagePattern(activeContact.getUser().getImage()));
        receiverNameLabel.setText(activeContact.getUser().getName());

        focusOnContact();
        if(activeContact.getUser().getStatus() == UserStatus.OFFLINE){
            messageTextArea.setDisable(true);
        } else {
            messageTextArea.setDisable(false);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getChildren().clear();
                loadMessageChat();
            }
        });
        /****************************************************************************************/
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
        notifications.add(notification);
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

        }else if(active == Active.Notifications){
            contactsList.getChildren().clear();
            notifications.stream().forEach(notification -> {
                contactsList.getChildren().add(new NotificationView(client, notification));
            });
        }
    }
    @Override
    public void recieveRecord(int senderId, byte[] buf) {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int sampleSize = 16;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        AudioBuffer audioBuffer = new AudioBuffer(ByteBuffer.wrap(buf), new org.jcodec.common.AudioFormat(44100, 16, 2, true, false), buf.length);

        //            FileOutputStream fileOutputStream=new FileOutputStream();
        // checks if system supports the data line
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
//            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
//
//            line.write(buf, 0, buf.length);
            for(int i=0; i<10; i++)
                System.out.println("Buf: " +  buf[i]);

        AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(buf), format, buf.length / format.getFrameSize());
        File wavFile = new File("./Client/src/main/resources/org/asasna/chat/client/audio/record2.wav");
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //AudioClip clip=new AudioClip(getClass().getResource("record2.wav").toExternalForm());

        Media media = new Media(Paths.get("./Client/src/main/resources/org/asasna/chat/client/audio/record2.wav").toUri().toString());
        AudioClip audioClip = new AudioClip(Paths.get("./Client/src/main/resources/org/asasna/chat/client/audio/record2.wav").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        HBox box = new HBox();
        //box.setStyle("-fx-background-color: red;-fx-pref-height: 20px");
        FontIcon playIcon = new FontIcon();
        playIcon.setIconLiteral("dashicons-controls-play");
        playIcon.setIconColor(Color.BLACK);
        playIcon.setIconSize(25);
        JFXButton startStop = new JFXButton(">");
        /*startStop.setOnAction(e->{
        box.setStyle("-fx-background-color: red;-fx-pref-height: 20px");
        JFXButton startStop = new JFXButton(">");
        startStop.setOnAction(e -> {
            mediaPlayer.play();
            mediaPlayer.seek(Duration.ZERO);
            /*if(mediaPlayer.getStatus()== MediaPlayer.Status.STOPPED){
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });*/
        playIcon.setOnMouseClicked(e -> {
            Platform.runLater(() -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    playIcon.setIconLiteral("dashicons-controls-play");
                    mediaPlayer.pause();
                } else {
                    playIcon.setIconLiteral("dashicons-controls-pause");
                    playIcon.setIconColor(Color.RED);
                    mediaPlayer.play();
                }

                // playIcon.setIconColor(Color.BLACK);
            });


        });

        mediaPlayer.setOnPlaying(() -> {
            playIcon.setIconColor(Color.RED);
        });
        mediaPlayer.setOnEndOfMedia(() -> {
            playIcon.setIconColor(Color.BLACK);
            playIcon.setIconLiteral("dashicons-controls-play");
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });
        JFXSlider slider = new JFXSlider();
        /*slider.setMax(mediaPlayer.getTotalDuration().toMillis());
        slider.setOnMouseClicked((MouseEvent mouseEvent) -> {
            mediaPlayer.seek(Duration.millis(slider.getValue()));
        });
        mediaPlayer.currentTimeProperty().addListener((ob, ol, nw) -> {
            slider.setValue(nw.toMillis());
        });*/
        Platform.runLater(() -> {
            slider.maxProperty().bind(Bindings.createDoubleBinding(
                    () -> mediaPlayer.getTotalDuration().toSeconds(),
                    mediaPlayer.totalDurationProperty()));
            mediaPlayer.currentTimeProperty().addListener((ob, ol, nw) -> {
                slider.setValue(nw.toSeconds());
            });
            slider.setOnMouseClicked((e) -> {
                mediaPlayer.seek(Duration.seconds(slider.getValue()));
            });
        });

        MediaView mediaView = new MediaView(mediaPlayer);
        box.setSpacing(10);
        box.getChildren().add(playIcon);
        box.getChildren().add(slider);
        Platform.runLater(() -> {
            view.getChildren().add(box);
        });

    }

    // End Elsayed Nabil


    //    Start Abdo
    public void recieveGroupMessage(ChatGroup group, Message message) {
        if (!contactsList.getChildren().parallelStream().filter(c -> c instanceof GroupContact).mapToInt(c -> ((GroupContact) c).getChatGroup().getGroupId()).anyMatch(i -> i == group.getGroupId())) {
            Platform.runLater(() -> {
                GroupContact contact = new GroupContact(group);
                contact.setOnMouseClicked((e) -> {
                    this.activeContact = contact;
                });
                contactsList.getChildren().add(0, contact);
                tempDisplayMessage(message);
            });
        } else {
            Platform.runLater(() -> {
                contactsList.getChildren().parallelStream()
                        .filter(c -> c instanceof GroupContact)
                        .filter(g -> ((GroupContact) g).getChatGroup().getGroupId() == group.getGroupId())
                        .findFirst()
                        .ifPresent(n -> {
                            contactsList.getChildren().remove(n);
                            contactsList.getChildren().add(0, n);
                        });
                    tempDisplayMessage(group.getGroupId(), message);
//                Notifications.create().title("New Message").text("Message from " + message.getUserId()).graphic(new Circle()).show();
            });

        }

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
                    int senderId = me.getId();
                    Message message = new Message(senderId,fileName, MessageType.FILE);
                    if (activeContact instanceof GroupContact)
                        client.sendGroupFileToServer(selectedFile.getPath(), fileExtension, ((GroupContact) activeContact).getChatGroup(), message);
                    else{
                        int friendId = activeContact.getUser().getId();
                        client.sendFileToServer(selectedFile.getPath(), fileExtension, friendId, message);
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    @Override
    public void tempFileDisplayMessage(Message message) {
        viewTextMessage = new MSGview(message,this);
        if (me.getId() == message.getUserId()) {///////////////////////////////////me
            System.out.println("Me: " + message.getMesssagecontent());
            viewTextMessage.setTextMSGview(SpeechDirection.RIGHT);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    view.getChildren().add(viewTextMessage);
                }
            });

        } else {
            System.out.println("Friend: " + message.getMesssagecontent());
            viewTextMessage.setTextMSGview(SpeechDirection.LEFT);
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    view.getChildren().add(viewTextMessage);
                }
            });
        }
    }


    @FXML
    public void changeUserStatus() {
        UserStatus myStatus;
        try {
            System.out.println("chatController");
            if (me.getStatus() == UserStatus.ONLINE) {
                status.setStyle("-fx-fill:  #FF8C00");
                myStatus = UserStatus.BUSY;

            } else if (me.getStatus() == UserStatus.BUSY) {
                status.setStyle("-fx-fill:  #8B0000");
                myStatus = UserStatus.AWAY;
            } else {
                status.setStyle("-fx-fill:  #33FF4B");
                myStatus = UserStatus.ONLINE;
            }
            me.setStatus(myStatus);
            client.changeStatus(me,myStatus);
            System.out.println("chatController2");
            //circle.setfill()//wel list bta3tha

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void updateMyContactList(User updatedUser) {

        Platform.runLater(() -> {
            ObservableList<Node> contacts;
            contacts = contactsList.getChildren();
            Contact myContact;
            // boolean newContact=false;
            for (Node c : contacts) {
                myContact = (Contact) c;
                if (updatedUser.getId() == myContact.getUser().getId()) {
                    // newContact=true;
                    contactsList.getChildren().remove(myContact);
                    // if(updatedUser.getStatus()!=UserStatus.OFFLINE) {
                    Contact myContact1 = new Contact(updatedUser);
                    contactsList.getChildren().add(myContact1);
                    myContact1.setOnMouseClicked(ev -> {
                        System.out.println("you clicked me");
                        activeContact = myContact1;
                    });
                    contactsList.getChildren().add(myContact);
                    //  }

                    break;
                }
            }
           /* if(newContact){
                myContact = new Contact(updatedUser);
                contactsList.getChildren().add(myContact);
            }*/
        });

    }

    @FXML
    public void signMeOut() {
        try {
            client.signOut(me.getId());
            PrimaryController.removeFile("KeepMeLoggedIn");
            System.exit(0);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
   /* public void  removeKeepMeLoggedInFile(){
        try {
            File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/KeepMeLoggedIn.xml");
            FileDeleteStrategy.FORCE.delete(rememberMeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    // End Aya


    //    Start Shimaa

    String savedFilePath = null;
    MessageView messageView;
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
        if (me.getId() == message.getUserId()) {
            showSenderMessage(message);
        } else {
            saveReceiverMessages(message.getUserId(), message);
            if (activeContact.getUser().getId() == message.getUserId()) {
                showReceiverMessage(message);
            } else {
                showMessageNotification(message);
            }
        }
    }

    public void tempDisplayMessage(int groupId, Message message) {
        messageView = new MessageView(message);
        if (me.getId() == message.getUserId()) {
            messageView.setDirection(SpeechDirection.RIGHT);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    view.getChildren().add(messageView);
                }
            });
        } else {
            if (activeContact instanceof GroupContact) {
                if (((GroupContact) activeContact).getChatGroup().getGroupId() == groupId) {
                    messageView.setDirection(SpeechDirection.LEFT);
                    messageView.setImage(((GroupContact)activeContact).getImage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            view.getChildren().add(messageView);
                        }
                    });
                } else {
                    System.out.println("Message:  " + message.getMesssagecontent() + " from  " + message.getUserId());
                }
            }
        }
        if(message.getMessageType() == MessageType.FILE){
            System.out.println("Null: " + messageView.getDisplayedText());
            messageView.getDisplayedText().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // Adding Download File Here
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File selectedDirectory = directoryChooser.showDialog(null);
                    new Thread(() -> {
                        try {
                            client.getFile(selectedDirectory.getAbsolutePath(), message.getMesssagecontent(), message.getUserId());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    System.out.println("Download File");
                }
            });
        }
        saveReceiverMessages(message.getUserId(), message);
    }

    @Override
    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }


//    private Map<Integer, List<Message>> receiverMessages = new HashMap<>();

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

    public void loadMessageChat() {
        if (receiverMessages.get(activeContact.getUser().getId()) != null) {
            List<Message> messages = receiverMessages.get(activeContact.getUser().getId()) ;
            //Collections.sort(messages);
            for (Message m : messages) {
                MessageView messageView = new MessageView(m);
                if (me.getId() == m.getUserId()) {
                    messageView.setDirection(SpeechDirection.RIGHT);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            view.getChildren().add(messageView);
                        }
                    });
                } else if (activeContact.getUser().getId() == m.getUserId()) {
                    messageView.setDirection(SpeechDirection.LEFT);
                    messageView.setImage(activeContact.getUser().getImage());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            view.getChildren().add(messageView);
                        }
                    });
                }
                System.out.println(m);
            }
        }
    }

    private void focusOnContact() {
        final Background focusBackground = new Background( new BackgroundFill( Color.valueOf("#045ba5"), CornerRadii.EMPTY, Insets.EMPTY ) );
        final Background unfocusBackground = new Background( new BackgroundFill( Color.valueOf("#1e82dc"), CornerRadii.EMPTY, Insets.EMPTY ) );

        activeContact.setOnMouseClicked( ( e ) ->
        {
            activeContact.requestFocus();
            activeContact.backgroundProperty().bind(Bindings
                    .when(activeContact.focusedProperty())
                    .then(focusBackground)
                    .otherwise(unfocusBackground));
        } );
    }

    private void setListnerForPressingEnter() {
        messageTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    try {
                        send();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setInitialContact(){
        try {
            friends = client.getFriendList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Contact initialContact = new Contact(friends.get(0));
        activeContact = initialContact;
        System.out.println("At initialize function ---> active user " + activeContact.getUser().getName());
        receiverImage.setFill(new ImagePattern(activeContact.getUser().getImage()));
        receiverNameLabel.setText(activeContact.getUser().getName());
        Label sayHello = new Label(" Say Hello to " + activeContact.getUser().getName() + " ");
        sayHello.setAlignment(Pos.CENTER);
        sayHello.setStyle("-fx-background-color: GAINSBORO; -fx-background-radius: 10;" +
                " -fx-font-size: 14px; -fx-font-family: Consolas; -fx-font-weight: bold");
        HBox hBox = new HBox(sayHello);
        hBox.setAlignment(Pos.CENTER);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getChildren().add(hBox);
            }
        });
    }

    private void showMessageNotification(Message message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    User notSelectedFriend = client.getUser(message.getUserId());
                    TrayNotification tray = new TrayNotification();
                    tray.setTitle(notSelectedFriend.getName());
                    tray.setMessage(message.getMesssagecontent());
                    tray.setRectangleFill(Paint.valueOf("#1e82dc"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.setImage(notSelectedFriend.getImage());
                    tray.showAndDismiss(Duration.seconds(8));
                } catch (RemoteException e) {
                    System.out.println("There is no user with this id");
                }
            }
        });
    }

    private void showSenderMessage(Message message) {
        messageView = new MessageView(message);
        messageView.setDirection(SpeechDirection.RIGHT);
        saveReceiverMessages(activeContact.getUser().getId(), message);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getChildren().add(messageView);
            }
        });
    }

    private void showReceiverMessage(Message message) {
        messageView = new MessageView(message);
        messageView.setDirection(SpeechDirection.LEFT);
        messageView.setImage(activeContact.getUser().getImage());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view.getChildren().add(messageView);
            }
        });
    }

    // End shimaa


    //    Start Abeer Emad
    Scene scene;
    public   void setScene(Scene scene){
        this.scene=scene;
    }
    @FXML
    public void ProfileButtonClicked() {


        ProfileController profileController = new ProfileController( me, this);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile" + ".fxml"));
        fxmlLoader.setController(profileController);
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.setRoot(parent);
        profileController.setScene(scene);



    }
    // End Abeer Emad


    //    Start Nehal Adel

    @FXML
    public void send() throws RemoteException {
        if (activeContact instanceof GroupContact) {
            System.out.println("inner");
            client.sendGroupMessage(((GroupContact) activeContact).getChatGroup(), new Message(client.getUser().getId(), messageTextArea.getText()));
        } else {
            if (activeContact.getUser().getStatus() == UserStatus.ONLINE) {
                if (messageTextArea.getText().length() !=0 && !messageTextArea.getText().equals(" ")) {
                    int receiverId = activeContact.getUser().getId();
                    int senderId = me.getId();
                    String messageContent = messageTextArea.getText();
                    messageTextArea.setText("");
                    Message message = new Message(senderId, messageContent, MessageType.TEXT);
                    sendMessage(receiverId, message);
                }
            }
        }
    }
    // End Nehal Adel
}

