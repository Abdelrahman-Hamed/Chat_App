package org.asasna.chat.server.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.asasna.chat.server.App;
import org.asasna.chat.server.controller.AuthenticationService;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ServerHomeController implements Initializable {
    @FXML
    PieChart chart;

    @FXML
    Button genderButton ;
    @FXML
    Button statusButton ;

    @FXML
    Pane chatsPane;
    @FXML
    Pane settingPane;
    @FXML
    Pane announcmentPane;


    public Button getGenderButton() {
        return genderButton;
    }

    public Button getStatusButton() {
        return statusButton;
    }

    App app;
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
        data = FXCollections.observableArrayList(new PieChart.Data("Males", 10)
                , new PieChart.Data("Females", 90));
                 chart.setData(data);


        genderButton.setOnAction((actionEvent)->{try {
            data = authenticationService.getGenderData();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            Platform.runLater(()->{
                chart.setData(data);
            });});
        statusButton.setOnAction((actionEvent)->{try {
            data = authenticationService.getStatusData();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            Platform.runLater(()->{
                chart.setData(data);
            });});

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
        Platform.runLater(()->{
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
        chart.setData(data);
    }

    @FXML
    public void closeService() throws RemoteException, NotBoundException {
        app.getReg().unbind("AuthenticationService");
    }
    @FXML
    public void chartsPage(){
        chatsPane.setDisable(false);
        chatsPane.setOpacity(1);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
    }
    @FXML
    public void homePage(){
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
    }
    @FXML
    public void settingsPage(){
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(false);
        settingPane.setOpacity(1);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
    }
    @FXML
    public void announcementsPage(){
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(false);
        announcmentPane.setOpacity(1);
    }
}
