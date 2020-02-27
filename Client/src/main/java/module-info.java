module org.asasna.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.rmi;
    requires org.kordamp.ikonli.javafx;
    requires org.controlsfx.controls;
    requires rmiio;
    requires com.jfoenix;
    requires commons.io;
    requires java.xml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    requires jcodec;
    requires notification;
    opens org.asasna.chat.client;
    opens org.asasna.chat.client.view to javafx.fxml;
    exports org.asasna.chat.client;
    exports org.asasna.chat.client.Controller;
}