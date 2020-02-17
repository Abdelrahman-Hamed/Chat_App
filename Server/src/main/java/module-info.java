module org.asasna.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.sql;
    requires mysql.connector.java;
    requires java.rmi;
    requires java.naming;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires rmiio;
    requires commons.io;

    opens org.asasna.chat.server to javafx.fxml;
    exports org.asasna.chat.server;
}