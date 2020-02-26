package org.asasna.chat.server.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.asasna.chat.server.App;
import org.asasna.chat.server.controller.AuthenticationService;
import org.controlsfx.control.ToggleSwitch;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ServerHomeController implements Initializable {
    @FXML
    PieChart chart;
    @FXML
    Button genderButton;
    @FXML
    Button statusButton;
    @FXML
    ToggleSwitch service;
    @FXML
    Pane chatsPane;
    @FXML
    Pane settingPane;
    @FXML
    Pane announcmentPane;
    @FXML
    Button sendAnnounce;
    @FXML
    TextArea announce;

    public Button getGenderButton() {
        return genderButton;
    }

    public Button getStatusButton() {
        return statusButton;
    }

    App app =new App(this);
    ObservableList<PieChart.Data> data;
    AuthenticationService authenticationService;

    {
        try {
            authenticationService = new AuthenticationService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setService(ToggleSwitch service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        genderButton.setOnAction((actionEvent) -> {
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
        });
        statusButton.setOnAction((actionEvent) -> {
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
        });

        service.setOnMouseClicked((event) -> {
            if (!service.isSelected()) {
                try {
                    System.out.println(app.getReg());
                    app.getReg().unbind("AuthenticationService");
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
                System.out.println("service off");
            } else {
                try {
                    app.getReg().rebind("AuthenticationService", app.getiAuthenticationService());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.out.println("service on");
            }
        });

        sendAnnounce.setOnAction((actionEvent) -> {
            System.out.println(announce.getText());
            announce.clear();
        });
        announce.setOnKeyReleased((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER && keyEvent.isShiftDown())
            {
                System.out.println(announce.getText());
                announce.clear();
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
        chart.setData(data);
    }

    @FXML
    public void closeService() throws RemoteException, NotBoundException {
        if (service.isSelected()) {
            service.fire();
            app.getReg().unbind("AuthenticationService");
            System.out.println("service off");
        } else {
            service.fire();
            app.getReg().rebind("AuthenticationService", app.getiAuthenticationService());
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
    }

    @FXML
    public void homePage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
    }

    @FXML
    public void settingsPage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(false);
        settingPane.setOpacity(1);
        announcmentPane.setDisable(true);
        announcmentPane.setOpacity(0);
    }

    @FXML
    public void announcementsPage() {
        chatsPane.setDisable(true);
        chatsPane.setOpacity(0);
        settingPane.setDisable(true);
        settingPane.setOpacity(0);
        announcmentPane.setDisable(false);
        announcmentPane.setOpacity(1);
    }

    public void setApp(App app) {
        this.app=app;
    }
}
