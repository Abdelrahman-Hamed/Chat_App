package org.asasna.chat.server.model.dao;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.asasna.chat.common.model.Notification;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;

import java.util.List;
import java.util.Map;

public interface IUserDao {
    List<User> getAllUsers();

    Map<Boolean, List<User>> getNonContactUsers(int meUserId);

    User getUser(int id);

    User getUser(String phoneNumber, String password);

    User getUser(String phoneNumber);



    boolean updateUser(int id, User user);

    boolean deleteUser(int id);

    boolean addUser(User user);


    List<User> getFriendList(User user);

    ObservableList<PieChart.Data> getUsersByGender();

    ObservableList<PieChart.Data> getUsersByCountry();

    ObservableList<PieChart.Data> getUsersByStatus();


    boolean setNotification(int fromUserId, int toUserId);

    boolean cancelNotification(int fromUserId, int toUserId);

    List<Notification> getNotification(int id);
    public void updateUserStatues(int id, UserStatus status);
}
