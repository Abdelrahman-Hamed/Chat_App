package org.asasna.chat.client.model;

import org.asasna.chat.common.model.ChatGroup;
import org.asasna.chat.common.model.UserStatus;

public class GroupContact extends Contact {
    private ChatGroup chatGroup;

    public GroupContact() {
    }

    public GroupContact(ChatGroup chatGroup) {
        super(chatGroup.getName(),chatGroup.getImage(), UserStatus.ONLINE);
        this.chatGroup = chatGroup;
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

    public void setChatGroup(ChatGroup chatGroup) {

        this.chatGroup = chatGroup;
    }
}
