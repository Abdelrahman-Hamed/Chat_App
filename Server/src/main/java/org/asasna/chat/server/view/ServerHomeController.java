package org.asasna.chat.server.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerHomeController implements Initializable {
    @FXML
    PieChart chart;
    ObservableList<PieChart.Data> data ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data =  FXCollections.observableArrayList(new PieChart.Data("Males",10)
        ,new PieChart.Data("Females",90));
        chart.setData(data);

    }

   @FXML
    private void close(ActionEvent event){
    Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
       stage.close();
   }
    @FXML
    private void min(MouseEvent event){
        Stage stage = (Stage) (((Node)event.getSource()).getScene().getWindow());
        stage.setIconified(true);
    }

}
