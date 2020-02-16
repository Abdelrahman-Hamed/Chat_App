package org.asasna.chat.client.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.asasna.chat.client.App;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.util.Validation;
import org.asasna.chat.common.service.IChatService;


import java.io.IOException;
import java.rmi.RemoteException;

public class PrimaryController {

    Client client;

    Scene scene;
    public PrimaryController(){
    }


    public void setScene(Scene scene){
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
    private Button loginButton;
    @FXML
    public void switchToLogin()  {
        try {
            App.setRoot("register");
        }
        catch(IOException e){
            System.out.println("no fxml file");
        }
    }

    @FXML
    public void phoneNumberChanged(KeyEvent evnet){
        if(!Validation.validatePhoneNumber(phoneNumber.getText())){
            errorPhoneNumber.setVisible(true);
            password.setDisable(true);
            loginButton.setDisable(true);
            errorPhoneNumber.setText("Not A Valid Phone Number");
        }
        else{
            password.setDisable(false);
            loginButton.setDisable(false);
            errorPhoneNumber.setVisible(false);
        }

    }
    @FXML
    public void loginButtonClicked(ActionEvent event){

        if(!Validation.validatePhoneNumber(phoneNumber.getText())){
            errorPhoneNumber.setVisible(true);
            errorPhoneNumber.setText("Not A Valid Phone Number");
        }
        else{
            System.out.println("Clicked");
            try {
                ChatController chatController = new ChatController();
                client = new Client(chatController);
                password.setDisable(false);
                errorPhoneNumber.setVisible(false);
                IChatService chatService = client.login(phoneNumber.getText(), password.getText());
                if(chatService == null){
                    System.out.println("Phone Number OR Password is Incorrect");
                }else{
                    chatController.setClient(client);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat" + ".fxml"));
                    fxmlLoader.setController(chatController);
                    Parent parent = fxmlLoader.load();
                    scene.setRoot(parent);

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


//            loadChatPage(event);
        }
    }

    public void loadChatPage(ActionEvent event){
        try {
            App.setRoot("chat");
        }
            catch(IOException e){
            System.out.println("no chat.fxml file");
        }
    }
}
