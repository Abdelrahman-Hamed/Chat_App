package org.asasna.chat.client.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.apache.commons.io.FileDeleteStrategy;
import org.asasna.chat.client.App;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.util.AES;
import org.asasna.chat.client.util.Validation;
import org.asasna.chat.common.service.IChatService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    Client client;

    Scene scene;
    /////////////////////////////////////////////////////////////////////////////////////////////keep me logged in
    int userID;
    ChatController chatController;
    String phoneNo;
    String pass;
    public Boolean setTheChatScene = false;

    public PrimaryController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInfoIfExist();
    }

    private void loadInfoIfExist() {

        File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/rememberme.xml");
        if (rememberMeFile.exists()) {
            String[] arr = readFromMyFile(rememberMeFile);
            phoneNumber.setText(arr[0]);
            password.setText(arr[1]);
            password.setDisable(false);
            loginButton.setDisable(false);
            errorPhoneNumber.setVisible(false);
            rememberMe.setSelected(true);


        }
    }


    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @FXML
    private TextField phoneNumber;

    @FXML
    private Text errorPhoneNumber;

    @FXML
    private PasswordField password;

    @FXML
    private Text errorPassword;

    @FXML
    private CheckBox rememberMe;

    @FXML
    private Button loginButton;


    @FXML
    public void switchToLogin() {
        try {
            App.setRoot("register");
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register" + ".fxml"));
//            Parent parent = fxmlLoader.load();
//            scene.setRoot(parent);
        } catch (IOException e) {
            serverIsDownHandler();
            System.out.println("no fxml file");
        }
    }

    @FXML
    public void phoneNumberChanged(KeyEvent evnet) {
        if (!Validation.validatePhoneNumber(phoneNumber.getText())) {
            errorPhoneNumber.setVisible(true);
            password.setDisable(true);
            loginButton.setDisable(true);
            errorPhoneNumber.setText("Not A Valid Phone Number");
        } else {
            password.setDisable(false);
            loginButton.setDisable(false);
            errorPhoneNumber.setVisible(false);
        }

    }


    public static void removeFile(String fileName) {
        try {
            File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/" + fileName + ".xml");
            FileDeleteStrategy.FORCE.delete(rememberMeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void loginButtonClicked(ActionEvent event) {

        if (!Validation.validatePhoneNumber(phoneNumber.getText())) {
            errorPhoneNumber.setVisible(true);
            errorPhoneNumber.setText("Not A Valid Phone Number");
        } else if (!Validation.validateLoginPassword(password.getText())) {
            errorPassword.setVisible(true);
            errorPassword.setText("Not A Valid English Password");
        } else {
            System.out.println("Clicked");
            /////////////////////////////////////////////////////////////////////////////////////keep me
            phoneNo = phoneNumber.getText();
            pass = password.getText();
            ////////////////////////////////////////////////////////////////////////////
            password.setDisable(false);
            errorPhoneNumber.setVisible(false);

            IChatService chatService = createChatService(phoneNumber.getText(), password.getText());
            if (chatService != null) {
                if (rememberMe.isSelected()) {
                    //createRememberMeFile(phoneNumber.getText(), password.getText());
                    createMyFile("rememberme");
                } else {
                    removeFile("rememberme");
                }

                /////////////////////////////////////////////////////////////////////////////////////////////keep me logged in
                try {
                    userID = client.getUserId();
                } catch (RemoteException e) {
                    serverIsDownHandler();
                   // e.printStackTrace();
                }


                setChatScene();

            }


//            loadChatPage(event);
        }
    }

    public void loadChatPage(ActionEvent event) {
        try {
            App.setRoot("chat");
        } catch (IOException e) {
            System.out.println("no chat.fxml file");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////keep me logged in
    public void signOut() {
        try {
            client.signOut(userID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IChatService createChatService(String myPhoneNumber, String myPassword) {
        IChatService chatService = null;
        try {
            chatController = new ChatController();
            client = new Client(chatController, this);
            Pair<String, IChatService> temp = client.login(myPhoneNumber, myPassword);
            if (temp != null) {
                chatService = temp.getValue();
                if (chatService == null) {
                    System.out.println(temp.getKey());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText(temp.getKey());
                    alert.show();
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return chatService;

    }

    public void setChatScene() {
        try {
            setTheChatScene = true;
            chatController.setClient(client);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat" + ".fxml"));
            fxmlLoader.setController(chatController);
            Parent parent = fxmlLoader.load();
            scene.setRoot(parent);
            chatController.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createMyFile(String fileName) {

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            ProcessingInstruction processingInstructionElement = document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\"");
            Element authElement = document.createElement("Auth");
            Element phoneNumberElement = document.createElement("Phonenumber");
            phoneNumberElement.setTextContent(phoneNo);
            authElement.appendChild(phoneNumberElement);
            Element passwordElement = document.createElement("Password");
            passwordElement.setTextContent(pass);
            authElement.appendChild(passwordElement);
            document.appendChild(processingInstructionElement);
            document.appendChild(authElement);
            StringWriter stringWriter = new StringWriter();
            Source source = new DOMSource(document.getDocumentElement());
            FileWriter rememberMeFile = new FileWriter("./Client/src/main/java/org/asasna/chat/client/Auth/" + fileName + ".xml");
            BufferedWriter bufferedWriter = new BufferedWriter(rememberMeFile);
//            Result result = new StreamResult(rememberMeFile);
            Result result = new StreamResult(stringWriter);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
            bufferedWriter.write(AES.encrypt(stringWriter.toString(), "mySecretKey"));
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] readFromMyFile(File myFile) {
        String[] arr = new String[2];
        try {
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            String content = "", temp;
            while ((temp = br.readLine()) != null) {
                content += temp;
            }
            br.close();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(AES.decrypt(content, "mySecretKey").getBytes());
            Document document = documentBuilder.parse(inputStream);
            Element authElement = document.getDocumentElement();
            String phoneNumberStr = authElement.getFirstChild().getTextContent();
            String passwordStr = authElement.getLastChild().getTextContent();
            arr[0] = phoneNumberStr;
            arr[1] = passwordStr;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return arr;

    }


    public void loadChatByDefault(File keepMeLoggedInFile) {

        String[] arr = readFromMyFile(keepMeLoggedInFile);
        phoneNo = arr[0];
        pass = arr[1];

        IChatService chatService = createChatService(phoneNo, pass);
        if (chatService != null) {

            /////////////////////////////////////////////////////////////////////////////////////////////keep me logged in
            try {
                userID = client.getUserId();
                System.out.println(userID);
            } catch (java.rmi.ConnectException ex) {
                 serverIsDownHandler();
                // ex.printStackTrace();
            }catch (RemoteException e) {
                e.printStackTrace();
            }
            removeFile("KeepMeLoggedIn");

            setChatScene();


        }

    }

    @FXML
    Button closeONDown; //close all app if server is down
    @FXML
    BorderPane disableServerDown;
    @FXML
    Pane serverIsDown;//enable if Server is down

    @FXML
    private void close() {
        closeONDown.setOnAction((actionEvent) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void serverIsDownHandler() {
        disableServerDown.setDisable(true);
        serverIsDown.setOpacity(1);
        serverIsDown.setDisable(false);
        close();
    }

}
