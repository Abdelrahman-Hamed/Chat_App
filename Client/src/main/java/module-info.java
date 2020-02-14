module org.asasna.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.asasna to javafx.fxml;
    exports org.asasna.chat.client;
}