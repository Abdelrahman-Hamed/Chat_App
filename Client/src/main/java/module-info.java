module org.asasna.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.rmi;
    requires org.kordamp.ikonli.javafx;
    requires rmiio;
    requires com.jfoenix;
    requires commons.io;
    requires java.xml;
    requires java.sql;
    requires java.desktop;
    requires java.xml;
    requires java.sql;
    opens org.asasna.chat.client;
    opens org.asasna.chat.client.view to javafx.fxml;
    exports org.asasna.chat.client;
}