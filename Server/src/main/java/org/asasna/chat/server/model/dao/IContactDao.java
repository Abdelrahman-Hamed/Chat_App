package org.asasna.chat.server.model.dao;

public interface IContactDao {
    boolean removeFriend(int meId, int friendId);
}
