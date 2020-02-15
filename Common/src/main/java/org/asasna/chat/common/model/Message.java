package org.asasna.chat.common.model;

public class Message {

    private int userId;
    private String messsagecontent;

    public void setUserId(int userId){
        this.userId = userId;
    }

    public void setMesssagecontent(String messsagecontent){
        this.messsagecontent = messsagecontent;
    }

    public int getUserId(){
        return userId;
    }
    public String getMesssagecontent(){
        return messsagecontent;
    }

}
