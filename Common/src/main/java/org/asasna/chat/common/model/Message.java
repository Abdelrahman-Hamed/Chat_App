package org.asasna.chat.common.model;

import javafx.scene.layout.HBox;

import java.io.Serializable;

public class Message implements Serializable {

    private int userId;
    private HBox audioMessageSlider;
    private String messsagecontent;
    private MessageType messageType;
    private  byte[] messsageAudioContent;
    public Message(int userId, String messsagecontent) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
    }
    public Message(int userId, String messsagecontent, MessageType messageType) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
        this.messageType = messageType;
    }
    public Message(int userId, byte[] messsageAudioContent){
        this.userId = userId;
        this.messsageAudioContent = messsageAudioContent;
        this.messageType =MessageType.AUDIO;
    }
    public Message (int userId , HBox audioMessageSlider){
        this.userId = userId;
        this.messageType =MessageType.AUDIO;
        this.audioMessageSlider=audioMessageSlider;
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
    public void setMesssageAudioContent (byte [] messsageAudioContent){ this.messsageAudioContent=messsageAudioContent;}
    public byte[] getMesssageAudioContent (){ return this.messsageAudioContent;}
    public void setaudioMessageSlider(HBox audioMessageSlider){this.audioMessageSlider=audioMessageSlider;}
    public HBox getaudioMessageSlider(){return audioMessageSlider;}
}