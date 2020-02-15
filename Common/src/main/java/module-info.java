module org.asasna.common {
    requires java.rmi;
    requires rmiio;
    requires javafx.base;
    requires javafx.controls;
    exports org.asasna.chat.common.model;
    exports org.asasna.chat.common.service;
}