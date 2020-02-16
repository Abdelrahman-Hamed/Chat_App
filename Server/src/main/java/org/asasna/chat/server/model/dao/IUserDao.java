package org.asasna.chat.server.model.dao;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.asasna.chat.common.model.User;

import java.util.List;

public interface IUserDao {
    List<User> getAllUsers();

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


}
