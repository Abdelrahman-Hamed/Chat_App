module org.asasna.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.asasna.common;
    requires java.rmi;
    requires org.kordamp.ikonli.javafx;
    opens org.asasna.chat.client.view to javafx.fxml;
    exports org.asasna.chat.client;
}