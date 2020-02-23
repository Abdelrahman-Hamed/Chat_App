package org.asasna.chat.common.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private NotificationType type;
    private User user;

    public Notification(NotificationType type, User user){
        this.type = type;
        this.user = user;
    }
    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
