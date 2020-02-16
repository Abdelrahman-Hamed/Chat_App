package org.asasna.chat.client.model;

import org.asasna.chat.common.model.ChatGroup;

public class GroupContact extends Contact {
    private ChatGroup chatGroup;

    public GroupContact() {
    }

    public GroupContact(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }
}
