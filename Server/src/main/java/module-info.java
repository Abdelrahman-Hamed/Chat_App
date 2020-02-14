module org.asasna.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.sql;
    requires mysql.connector.java;
    requires java.rmi;

    opens org.asasna.chat.server to javafx.fxml;
    exports org.asasna.chat.server;
}