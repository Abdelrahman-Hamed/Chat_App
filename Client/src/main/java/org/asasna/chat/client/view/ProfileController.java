package org.asasna.chat.client.view;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.util.Validation;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.service.IChatService;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    User user;
    ChatController chatController;


    public ProfileController(User user, ChatController chatController) {
        this.user = user;
        this.chatController=chatController;


    }

    @FXML
    JFXTextField Name;
    @FXML
    JFXTextField Phone;
    @FXML
    JFXTextField Email;
    @FXML
    JFXTextField Country;
    @FXML
    JFXTextField NewPass;
    @FXML
    JFXTextField ConfirmPass;
    @FXML
    TextField check;


    @FXML
    Circle Photo;
    Client client ;

    Image image;
    Scene scene;
    public void setScene(Scene scene){
        this.scene=scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        check.setVisible(false);
        Name.setText(user.getName());
        Phone.setText(user.getPhone());
        Email.setText(user.getEmail());
        Country.setText(user.getCountry());
        Country.setDisable(true);
        Email.setDisable(true);
        Name.setDisable(true);
        Phone.setDisable(true);
        NewPass.setDisable(true);
        ConfirmPass.setDisable(true);


        //lssa tsama3 li ba2i al users



    }
@FXML
    public void UPdateButton() {
        Country.setDisable(false);
        Email.setDisable(false);
        Name.setDisable(false);
        Phone.setDisable(false);
        NewPass.setDisable(false);
        ConfirmPass.setDisable(false);



    }


    @FXML
    public void SaveButton(){
         Client client = chatController.client;
         IChatService chatService= client.chatService;
         if (checkingEmail() && checkingPhoneNum() && checkingName() && checkingConfirmPassword()) {
             user.setName(Name.getText());
             if(NewPass.getText().trim().length() > 0)
                user.setPassword(NewPass.getText().trim());
             user.setEmail(Email.getText());
             user.setCountry(Country.getText());
             user.setPhone(Phone.getText());
             if(image != null)
                user.setImageURL(image.getUrl());
             System.out.println(image.getUrl());

             try {
                 chatService.UpdateUser(user);
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
            check.setVisible(false);
         } else {
            check.setVisible(true);
         }

    }
    @FXML
    public void CancelButton (){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat" + ".fxml"));
        fxmlLoader.setController(chatController);
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.setRoot(parent);


    }
    public boolean checkingPhoneNum() {
        if (Phone.getText().equals("")) {

            return false;
        } else if (!Validation.validatePhoneNumber(Phone.getText())) {

            return false;
        } else {

            return true;
        }

    }

    public boolean checkingName() {
        if (Name.getText().equals("")) {

            return false;
        } else {

            return true;
        }
    }

    public boolean checkingEmail() {
        if (Email.getText().equals("")) {
            return false;
        } else if (!Validation.validateEmail(Email.getText())) {
            return false;
        } else {
            return true;
        }
    }
    public boolean checkingPassword() {
        if (NewPass.getText().equals("")) {

            return false;
        } else if (!Validation.validatePassword(NewPass.getText())) {

            return false;
        } else {

            return true;
        }

    }
    public boolean checkingConfirmPassword() {
        if (!Validation.validateConfiermedPassword(NewPass.getText(),ConfirmPass.getText())) {

            return false;
        } else {

            return true;
        }

    }

@FXML
public void editPhoto() {
    FileChooser open = new FileChooser();
   open.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
    File file = open.showOpenDialog(null);
    if (file != null) {
        Image image1 = new Image(file.toURI().toString());
        image = image1;

        Photo.setFill(new ImagePattern(image1));
    }

}

}
