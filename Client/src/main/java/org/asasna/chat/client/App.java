package org.asasna.chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.client.view.PrimaryController;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private  static PrimaryController primaryController;
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 900, 600);
        stage.setScene(scene);
        primaryController.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));

        Parent parent = fxmlLoader.load();
        primaryController = (PrimaryController) fxmlLoader.getController();

        return parent;
    }

    public static void main(String[] args) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("chat.fxml"));
        IChatController chatController=loader.getController();
        /*try {
            Client c = new Client(chatController);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }*/
        launch();

    }

}