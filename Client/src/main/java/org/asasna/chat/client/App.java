package org.asasna.chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.asasna.chat.client.Controller.Client;
import org.asasna.chat.client.model.IChatController;
import org.asasna.chat.client.util.AES;
import org.asasna.chat.client.view.Controller;
import org.asasna.chat.client.view.PrimaryController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.rmi.RemoteException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static PrimaryController primaryController; // edited

    @Override
    public void start(Stage stage) throws IOException {
            scene = new Scene(loadFXML("login"));
            stage.setScene(scene);
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            primaryController.setScene(scene);
            stage.show();
        File keepMeLoggedInFile = new File("./Client/src/main/java/org/asasna/chat/client/Auth/KeepMeLoggedIn.xml");
        if (keepMeLoggedInFile.exists()) {
            primaryController.loadChatByDefault(keepMeLoggedInFile);
        }
        stage.show();

    }


    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        if(fxmlLoader.getController() instanceof PrimaryController){
            primaryController = (PrimaryController) fxmlLoader.getController();
        }
        return parent;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() {
        if(primaryController.setTheChatScene) {
            primaryController.createMyFile("KeepMeLoggedIn");
            primaryController.signOut();
        }
        System.exit(0);

    }


}