package org.asasna.chat.common.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    int userId;
    String messageContent;
    MessageType messageType;
    Message message;
    MessageTest(){
        userId = 1;
        messageContent = "Hello Sayed";
        messageType = MessageType.TEXT;
        message = new Message(userId, messageContent, messageType);
    }
    @Test
    void getUserId() {
        Assertions.assertEquals(userId, message.getUserId());
    }

    @Test
    void getMesssagecontent() {
        Assertions.assertEquals(messageContent, message.getMesssagecontent());
        Assertions.assertNotNull(message.getMesssagecontent());
    }

    @Test
    void getMessageType() {
        Assertions.assertEquals(messageType, message.getMessageType());
        Assertions.assertNotNull(message.getMessageType());
    }
}