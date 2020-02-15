package org.asasna.chat.common.model;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.List;

public class ChatGroup implements Serializable {

    private int groupId;
    private List<Integer> participents;
    private transient Image image;
    private String name;

    public ChatGroup() {
    }

    public ChatGroup(int groupId, List<Integer> groupUsers, Image image) {
        this.groupId = groupId;
        this.participents = groupUsers;
        this.image = image;
    }

    public ChatGroup(int groupId, List<Integer> participents, String name) {
        this.groupId = groupId;
        this.participents = participents;
        this.name = name;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Integer> getParticipents() {
        return participents;
    }

    public void setParticipents(List<Integer> participents) {
        this.participents = participents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
