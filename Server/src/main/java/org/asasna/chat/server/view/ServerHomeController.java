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
    ObservableList<PieChart.Data> data;
    AuthenticationService authenticationService;

    {
        try {
            authenticationService = new AuthenticationService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    App app;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      /*  data = FXCollections.observableArrayList(new PieChart.Data("Males", 10)
                , new PieChart.Data("Females", 90));
        */
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

        this.getStatusData();

    }


    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.close();
    }

    @FXML
    private void min(MouseEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.setIconified(true);
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
}
