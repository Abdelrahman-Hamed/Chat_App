package org.asasna.chat.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.asasna.chat.common.service.IAuthenticationService;
import org.asasna.chat.server.controller.AuthenticationService;
import org.asasna.chat.server.view.ServerHomeController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import org.asasna.chat.common.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    //private static Stage primaryStage;
    Registry reg;
    public static ServerHomeController controller;
    IAuthenticationService iAuthenticationService ;
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            iAuthenticationService=new AuthenticationService();
            reg= LocateRegistry.createRegistry(5000);
            reg.rebind("AuthenticationService", iAuthenticationService );

        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        scene = new Scene(loadFXML("server"));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            try {
                reg.unbind("AuthenticationService");
                Platform.exit();
                System.exit(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        controller = (ServerHomeController) fxmlLoader.getController();
        return parent;
    }

    public Registry getReg() {
        return reg;
    }

    public static void main(String[] args) {
        launch();
    }

}