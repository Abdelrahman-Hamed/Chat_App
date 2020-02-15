package org.asasna.chat.common.model;

import java.io.Serializable;

public class Message implements Serializable {

    private int userId;
    private String messsagecontent;

    public Message(int userId, String messsagecontent) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
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

}