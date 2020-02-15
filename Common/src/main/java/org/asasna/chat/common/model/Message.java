package org.asasna.chat.common.model;

public class Message {
    private int id;
    private String text ;
    private String format;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getFormat() {
        return format;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Message(){}

    public Message(int id, String text) {
        this.text = text;
    }

    public Message(int id, String text,String format) {
        this.id = id;
    }

    public Message(int userId, String messsagecontent) {
        this.userId = userId;
        this.messsagecontent = messsagecontent;
    }

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
