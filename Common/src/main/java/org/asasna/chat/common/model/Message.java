package org.asasna.chat.common.model;

import java.io.Serializable;

public class Message implements Serializable {

    private int userId;
    private String messsagecontent;
    private MessageType messageType;
    public Message(int userId, String messsagecontent) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
    }
    public Message(int userId, String messsagecontent, MessageType messageType) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
        this.messageType = messageType;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMesssagecontent(String messsagecontent) {
        this.messsagecontent = messsagecontent;
    }

    public int getUserId() {
        return userId;
    }

    public String getMesssagecontent() {
        return messsagecontent;
    }

    public String toString(){
        return "User Id is " + userId + " , and Message content is " + messsagecontent;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}