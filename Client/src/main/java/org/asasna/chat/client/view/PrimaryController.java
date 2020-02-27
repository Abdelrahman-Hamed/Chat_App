package org.asasna.chat.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
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

public class PrimaryController implements Initializable{

    Client client;

    Scene scene;

    public PrimaryController() {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInfoIfExist();
    }
    private void loadInfoIfExist(){
        File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/rememberme.xml");
        if(rememberMeFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(rememberMeFile));
                String content = "", temp;
                while((temp=br.readLine()) != null){
                    content += temp;
                }
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputStream inputStream = new ByteArrayInputStream(AES.decrypt(content, "mySecretKey").getBytes());
                Document document = documentBuilder.parse(inputStream);
                Element authElement = document.getDocumentElement();
                String phoneNumberStr = authElement.getFirstChild().getTextContent();
                String passwordStr = authElement.getLastChild().getTextContent();
                phoneNumber.setText(phoneNumberStr);
                password.setText(passwordStr);
                password.setDisable(false);
                loginButton.setDisable(false);
                errorPhoneNumber.setVisible(false);
                rememberMe.setSelected(true);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    private CheckBox KeepMeLoggedIn;

    @FXML
    public void switchToLogin() {
        try {
            //App.setRoot("register");
            RegisterController registerController = new RegisterController();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register" + ".fxml"));
            fxmlLoader.setController(registerController);
            Parent parent = fxmlLoader.load();
            scene.setRoot(parent);
        }
        catch(IOException e){
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
    private void createRememberMeFile(String userName, String password){
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            ProcessingInstruction processingInstructionElement = document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\"");
            Element authElement = document.createElement("Auth");
            Element phoneNumberElement = document.createElement("Phonenumber");
            phoneNumberElement.setTextContent(userName);
            authElement.appendChild(phoneNumberElement);
            Element passwordElement = document.createElement("Password");
            passwordElement.setTextContent(password);
            authElement.appendChild(passwordElement);
            document.appendChild(processingInstructionElement);
            document.appendChild(authElement);
            StringWriter stringWriter = new StringWriter();
            Source source = new DOMSource(document.getDocumentElement());
            FileWriter rememberMeFile = new FileWriter("./Client/src/main/java/org/asasna/chat/client/Auth/rememberme.xml");
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
    private void removeRememberMeFile(){
        try {
            File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/rememberme.xml");
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
        } else {
            System.out.println("Clicked");
            try {
                ChatController chatController = new ChatController();
                client = new Client(chatController);
                password.setDisable(false);
                errorPhoneNumber.setVisible(false);

                IChatService chatService = client.login(phoneNumber.getText(), password.getText());
                if (chatService == null) {
                    System.out.println("Phone Number OR Password is Incorrect");
                }else{
                    if(rememberMe.isSelected()){
                        createRememberMeFile(phoneNumber.getText(), password.getText());
                    }else{
                        removeRememberMeFile();
                    }

                    if(KeepMeLoggedIn.isSelected()){
                        int userID=client.getUserId();
                        createKeepMeLoggedInFile(userID);

                    }else{
                        removeKeepMeLoggedInFile();
                    }

                    chatController.setClient(client);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat" + ".fxml"));
                    fxmlLoader.setController(chatController);
                    Parent parent = fxmlLoader.load();
                    scene.setRoot(parent);
                    chatController.setScene(scene);
             }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
    public void  removeKeepMeLoggedInFile(){
        try {
            File rememberMeFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/KeepMeLoggedIn.xml");
            FileDeleteStrategy.FORCE.delete(rememberMeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void  createKeepMeLoggedInFile(int id) {

        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            ProcessingInstruction processingInstructionElement = document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\"");
            Element authElement = document.createElement("Auth");
            Element useId = document.createElement("Phonenumber");
            useId.setTextContent(String.valueOf(id));
            authElement.appendChild(useId);
            document.appendChild(processingInstructionElement);
            document.appendChild(authElement);
            StringWriter stringWriter = new StringWriter();
            Source source = new DOMSource(document.getDocumentElement());
            FileWriter rememberMeFile = new FileWriter("./Client/src/main/java/org/asasna/chat/client/Auth/KeepMeLoggedIn.xml");
            BufferedWriter bufferedWriter = new BufferedWriter(rememberMeFile);
//            Result result = new StreamResult(rememberMeFile);
            Result result = new StreamResult(stringWriter);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);
            bufferedWriter.write(AES.encrypt(stringWriter.toString(), "mySecretKey"));
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (ParserConfigurationException|TransformerException |IOException e) {
            e.printStackTrace();
        }
    }
}
