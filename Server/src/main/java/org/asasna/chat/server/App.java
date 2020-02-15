package org.asasna.chat.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asasna.chat.common.service.IChatService;
import org.asasna.chat.server.services.ChatService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
//import org.asasna.chat.common.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
       /* try {
            IChatService iChatService=new ChatService();
            Registry reg= LocateRegistry.createRegistry(5000);
            reg.rebind("ChatService",iChatService );
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();*/
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //launch();
        try {
            IChatService iChatService=new ChatService();
            Registry reg= LocateRegistry.createRegistry(5000);
            reg.rebind("ChatService",iChatService );
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

}