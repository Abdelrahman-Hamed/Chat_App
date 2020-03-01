package org.asasna.chat.common.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    Notification notification;
    NotificationType type;
    User user;
    NotificationTest(){
        type = NotificationType.FRIEND_REQUEST;
        user = new User(1, "Elsayed Nabil", "011279425232", "sayed@gmail.com", "123456789", Gender.Male, null, null, null, UserStatus.ONLINE, null, false, false);
        notification = new Notification(type, user);
    }
    @Test
    void getType() {
    }

    @Test
    void setType() {
    }

    @Test
    void getUser() {
    }
}