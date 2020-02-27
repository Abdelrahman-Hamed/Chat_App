module org.asasna.server {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.sql;
    requires mysql.connector.java;
    requires java.rmi;
    requires java.naming;
    requires java.desktop;
    requires rmiio;
    requires commons.io;
    requires log4j;
    requires org.kordamp.ikonli.javafx;
    requires javafx.swing;
    requires org.asasna.client;
    opens org.asasna.chat.server to javafx.fxml;
    opens org.asasna.chat.server.view to javafx.fxml;
    exports org.asasna.chat.server;
}