package org.asasna.chat.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import javafx.stage.WindowEvent;
import org.asasna.chat.common.service.IAuthenticationService;
import org.asasna.chat.server.controller.AuthenticationService;
import org.asasna.chat.server.view.ServerHomeController;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
//import org.asasna.chat.common.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    Registry reg;
    public ServerHomeController controller;
    public static IAuthenticationService iAuthenticationService ;
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            iAuthenticationService=new AuthenticationService();
            reg= LocateRegistry.createRegistry(5001);
            reg.rebind("AuthenticationService", iAuthenticationService );

        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
        scene = new Scene(loadFXML("server"));
        primaryStage.setScene(scene);
        controller.setApp(this);
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            try {
                if (iAuthenticationService.getThisChatService() != null){
                    iAuthenticationService.getThisChatService().closeServer();
                    iAuthenticationService.getThisChatService().unRegisterAll();
                }
                reg.unbind("AuthenticationService");
                Platform.exit();
                System.exit(0);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                System.out.println("already unbound");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        controller = (ServerHomeController) fxmlLoader.getController();
        return parent;
    }

    public Registry getReg() {
        return reg;
    }

    public static void main(String[] args) {
      //  try {
            /*
            System.setProperty("javax.net.ssl.keyStore","/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmsim.ks");
            System.setProperty("javax.net.ssl.keyStorePassword","123456");
            System.setProperty("javax.net.ssl.trustStore","/home/abdulrahman/IdeaProjects/ITI_Chat/sysdmtruststore.ks");
            System.setProperty("javax.net.ssl.trustStorePassword","123456");
            //System.setProperty("java.rmi.server.hostname", "10.145.4.235");
            RMIClientorg.asasna.chat.client.App.loadFXMLSocketFactory rmicsf = new SslRMIClientSocketFactory();
            RMIServerSocketFactory rmissf = new SslRMIServerSocketFactory();
            Registry reg = LocateRegistry.createRegistry(5001, rmicsf, rmissf);
            IAuthenticationService iAuthenticationService = new AuthenticationService();

             */
          //  Registry reg = LocateRegistry.createRegistry(5001);
           // IAuthenticationService iAuthenticationService2 = new AuthenticationService();
            Logger logger= LogManager.getLogger(App.class);
            //BasicConfigurator.configure();
            logger.info("Server Started");
           // reg.rebind("AuthenticationService", iAuthenticationService2);
           // Thread.sleep(Long.MAX_VALUE);
       // }
        /*catch (RemoteException | InterruptedException ex) {
            ex.printStackTrace();
        }*/
        launch();
    }

    public IAuthenticationService getiAuthenticationService() {
        return iAuthenticationService;
    }
}