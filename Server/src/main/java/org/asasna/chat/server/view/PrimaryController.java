package org.asasna.chat.server.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.asasna.chat.server.App;
import org.asasna.chat.server.util.Validation;

import java.io.IOException;

import static org.asasna.chat.server.App.*;

public class PrimaryController {

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
    private void phoneNumberChanged(KeyEvent evnet){
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
    private void loginButtonClicked(ActionEvent event){
        if(!Validation.validatePhoneNumber(phoneNumber.getText())){
            errorPhoneNumber.setVisible(true);
            errorPhoneNumber.setText("Not A Valid Phone Number");
        }
        else{
            password.setDisable(false);
            errorPhoneNumber.setVisible(false);
            loadHomePage(event);
        }
    }

    private void loadHomePage(ActionEvent event){
        try {
            setRoot("server");
        }
            catch(IOException e){
            System.out.println("no server.fxml file");
        }
    }

    @FXML
    private void close(ActionEvent event){
        Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        stage.close();
    }
    @FXML
    private void min(ActionEvent event){
        Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }
}
