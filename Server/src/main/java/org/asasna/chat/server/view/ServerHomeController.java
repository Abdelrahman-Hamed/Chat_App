package org.asasna.chat.server.view;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.asasna.chat.common.model.*;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.App;
import org.asasna.chat.server.controller.AuthenticationService;
import org.asasna.chat.server.services.ChatService;
import org.asasna.chat.server.util.Validation;
import org.controlsfx.control.ToggleSwitch;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class ServerHomeController implements Initializable {
    @FXML
    PieChart chart;
    @FXML
    Button genderButton,statusButton,contryButton,home, announcements,settings,charts,loginButton,sendAnnounce;
    @FXML
    ToggleSwitch service;
    @FXML
    Pane chatsPane,settingPane, announcmentPane,homePane;
    @FXML
    TextArea announce,bio;
    @FXML
    TextField phoneNumber,RegPhoneNumber,regPassword,confirmRegPassword,email,name;
    @FXML
    PasswordField password;
    @FXML
    ComboBox<String> countryBox;
    @FXML
    RadioButton female,male;
    @FXML
    Text confirmRegestered,nameError,ConfirmePasswordError,radioButtonError,countryError, dateError, bioError,emailError,passwordError,errorRegPhoneNumber,errorPhoneNumber ,errorPassword;
    @FXML
    DatePicker dateOfBirth;
    {

        defaultImage = new Image(getClass().getResource("abdo.jpg").toExternalForm());
        confirmRegestered = new Text();
    }
    ToggleGroup tg;
    ArrayList<String> countries;
    Image defaultImage;
    private Image image;

    public Button getGenderButton() {
        return genderButton;
    }

    public Button getStatusButton() {
        return statusButton;
    }

    App app =new App();
    ObservableList<PieChart.Data> data;
    AuthenticationService authenticationService;

    {
        try {
            authenticationService = new AuthenticationService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homePage();
        tg = new ToggleGroup();
        female.setToggleGroup(tg);
        male.setToggleGroup(tg);
        countries = new ArrayList<String>();
        String[] countryCodes = Locale.getISOCountries();
        for (int i = 0; i < countryCodes.length; i++) {
            Locale obj = new Locale("", countryCodes[i]);
            //  countries.add( obj.getDisplayCountry());
            countryBox.getItems().addAll(obj.getDisplayCountry());
            countries.add(obj.getDisplayCountry().toLowerCase());

        }
        genderButton.setOnAction((actionEvent) -> {
            getGenderData();
        });
        statusButton.setOnAction((actionEvent) -> {
            getStatusData();
        });
        contryButton.setOnAction((actionEvent) -> {
            try {
                data = authenticationService.getCountryData();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                chart.setData(data);
            });
        });
        service.setOnMouseClicked((event) -> {
            closeService();
        });

        sendAnnounce.setOnAction((actionEvent) -> {
            System.out.println(announce.getText());
            announce.clear();
        });
        announce.setOnKeyReleased((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER && keyEvent.isShiftDown())
            {
                try {
                    sendAnnouncements();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                announce.clear();
            }

        });

        homePane.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER )
            {
                login();
            }

        });
    }

    @FXML
    public void send(ActionEvent event){
        System.out.println(announce.getText());
        announce.clear();
    }
    @FXML
    public void getGenderData() {
        try {
            data = authenticationService.getGenderData();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            chart.setData(data);
        });
    }

    @FXML
    public void getStatusData() {
        try {
            data = authenticationService.getStatusData();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            chart.setData(data);
        });
    }

    @FXML
    public void closeService(){
        if (!service.isSelected()) {
            try {
                System.out.println(app.getReg());
                App.iAuthenticationService.getThisChatService().closeServer();
                App.iAuthenticationService.getThisChatService().unRegisterAll();
                app.getReg().unbind("AuthenticationService");
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            System.out.println("service off");
        } else {
            try {
                app.getReg().rebind("AuthenticationService", App.iAuthenticationService);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("service on");
        }
    }

    @FXML
    public void chartsPage() {
        chatsPane.setDisable(false);
        chatsPane.setOpacity(1);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
        homePane.setOpacity(0);
        homePane.setDisable(true);
    }

    @FXML
    public void homePage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
        homePane.setOpacity(1);
        homePane.setDisable(false);
    }

    @FXML
    public void settingsPage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(false);
        settingPane.setOpacity(1);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
        homePane.setOpacity(0);
        homePane.setDisable(true);
    }

    @FXML
    public void announcementsPage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(false);
        announcmentPane.setOpacity(1);
        homePane.setOpacity(0);
        homePane.setDisable(true);
    }

    @FXML
    public void loginButtonClicked(ActionEvent event) {
        login();
    }

    public void setApp(App app) {
        this.app=app;
    }

    public boolean checkingRadioButtons() {

        if (tg.getSelectedToggle() == null) {
            radioButtonError.setVisible(true);
            radioButtonError.setText("Required field");
            return false;
        } else {
            radioButtonError.setVisible(false);
            return true;
        }

    }

    public boolean checkDatePicker() {
        if (dateOfBirth.getValue() == null) {
            dateError.setVisible(true);
            dateError.setText("Required field");
            return false;
        } else {
            dateError.setVisible(false);
            return true;
        }

    }

    public boolean checkingPhoneNum() {
        if (RegPhoneNumber.getText().equals("")) {
            errorRegPhoneNumber.setVisible(true);
            errorRegPhoneNumber.setText("Required field");
            return false;
        } else if (!Validation.validatePhoneNumber(RegPhoneNumber.getText())) {
            errorRegPhoneNumber.setVisible(true);
            errorRegPhoneNumber.setText("Not a Valid Phone Number");
            return false;
        } else {
            errorRegPhoneNumber.setVisible(false);
            return true;
        }

    }

    private void login(){
        if (!phoneNumber.getText().equals("adminPhone") || !password.getText().equals("adminPass")) {
            errorPhoneNumber.setVisible(true);
            errorPhoneNumber.setText("Not A UserName !");
            errorPassword.setVisible(true);
            errorPassword.setText("Not A Password !");

        } else {
            System.out.println("Admin Loggedin");
            home.setDisable(true);
            announcements.setDisable(false);
            settings.setDisable(false);
            charts.setDisable(false);
            this.settingsPage();
        }
    }
    public boolean checkingName() {
        if (name.getText().equals("")) {
            nameError.setVisible(true);
            nameError.setText("Required field");
            return false;
        } else {
            nameError.setVisible(false);
            return true;
        }
    }

    public boolean checkingEmail() {
        if (email.getText().equals("")) {
            emailError.setVisible(true);
            emailError.setText("Required field");
            return false;
        } else if (!Validation.validateEmail(email.getText())) {
            emailError.setVisible(true);
            emailError.setText("Not a valid email");
            return false;
        } else {
            emailError.setVisible(false);
            return true;
        }
    }

    public boolean checkComboBox() {

        if (countryBox.getSelectionModel().isEmpty()) {

            if (countryBox.getValue() != null && countries.contains(countryBox.getValue().toLowerCase())) {
                countryError.setVisible(false);
                return true;
            } else if (countryBox.getValue() == null || countryBox.getValue().equals("")) {
                countryError.setVisible(true);
                countryError.setText("Required field");
                return false;
            } else {
                countryError.setVisible(true);
                countryError.setText("Not a valid country name");
                return false;
            }

        } else {
            countryError.setVisible(false);
            return true;
        }


    }

    public boolean checkBio() {
        if (bio.getText().equals("")) {
            bioError.setVisible(true);
            bioError.setText("Required field");
            return false;
        } else {
            bioError.setVisible(false);
            return true;
        }
    }

    @FXML
    public boolean checkingPassword() {
        if (!confirmRegPassword.getText().equals("")) {
            checkingConfirmPassword();
        }
        if (regPassword.getText().equals("")) {
            passwordError.setVisible(true);
            passwordError.setText("Required field");
            return false;
        } else if (!Validation.validatePassword(regPassword.getText())) {
            passwordError.setVisible(true);
            passwordError.setText("Not a Valid Password");
            return false;
        } else {
            passwordError.setVisible(false);
            return true;
        }

    }

    @FXML
    public boolean checkingConfirmPassword() {
        if (confirmRegPassword.getText().equals("")) {
            ConfirmePasswordError.setVisible(true);
            ConfirmePasswordError.setText("Required field");
            return false;
        } else if (!Validation.validateConfiermedPassword(confirmRegPassword.getText(), regPassword.getText())) {
            ConfirmePasswordError.setVisible(true);
            ConfirmePasswordError.setText("Not matched");
            return false;
        } else {
            ConfirmePasswordError.setVisible(false);
            return true;
        }
    }


    @FXML
    public void handleSubmitAction() {
        if (checkingPhoneNum() & checkingRadioButtons() & checkComboBox() & checkBio() & checkingName() & checkingEmail() & checkingPassword() & checkingConfirmPassword() & checkDatePicker()) {
            try {
                User me = new User(name.getText(), RegPhoneNumber.getText(), email.getText(), regPassword.getText(), getSelectedGender(), countryBox.getValue(), convertToDateViaSqlDate(dateOfBirth.getValue()), bio.getText(), UserStatus.OFFLINE, "abdo.jpg", false, false);
                if (image == null)
                    me.setImage(defaultImage);
                else
                    me.setImage(image);
                if (addingUser(me)) {
                    handleResetAction();
                    confirmRegestered.setText("registered successfully !");

                } else {
                    confirmRegestered.setText("phone already registered !");
                }
            } catch (IOException e) {
                System.out.println("no fxml file");
            }
        }
    }

    @FXML
    public void handleResetAction() {
        RegPhoneNumber.setText("");
        name.setText("");
        regPassword.setText("");
        confirmRegPassword.setText("");
        email.setText("");
        bio.setText("");
        countryBox.getSelectionModel().select(0);
        countryBox.getSelectionModel().clearSelection();
        countryBox.setPromptText("Country");
        dateOfBirth.setValue(null);
        female.setSelected(false);
        male.setSelected(false);
        errorRegPhoneNumber.setVisible(false);
        nameError.setVisible(false);
        passwordError.setVisible(false);
        ConfirmePasswordError.setVisible(false);
        emailError.setVisible(false);
        dateError.setVisible(false);
        bioError.setVisible(false);
        countryError.setVisible(false);
        radioButtonError.setVisible(false);
    }

    private Gender getSelectedGender() {
        return male.isSelected() ? Gender.Male : Gender.Female;
    }

    public boolean addingUser(User me) throws RemoteException {
        System.out.println("ide = " + me.getId());
        System.out.println("name =" + me.getName());
        System.out.println("phone =" + me.getPhone());
        System.out.println("mail =" + me.getEmail());
        System.out.println("password =" + me.getPassword());
        System.out.println("gender =" + me.getGender());
        System.out.println("country =" + me.getCountry());
        System.out.println("bio =" + me.getBio());
        System.out.println("birth Date =" + me.getDateOfBirth());
        System.out.println("status =" + me.getStatus());
        System.out.println("image URL =" + me.getImageURL());
        System.out.println("verified =" + me.isVerified());
        System.out.println("chat bot  =" + me.isChatbotOption());
        if (authenticationService.isValid(me)) {
            authenticationService.addUser(me);
            return true;
        } else return false;
    }


    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public void sendAnnouncements() throws RemoteException {
        Message message=new Message(8000 ,announce.getText(), MessageType.TEXT);
        IChatService  iChatService = new ChatService();
        System.out.println("At server home ----> " + message);
        iChatService.sendAnnouncementsToOnlineUsers(message);

    }

}
