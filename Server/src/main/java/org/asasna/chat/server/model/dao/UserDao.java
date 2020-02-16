package org.asasna.chat.server.model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.asasna.chat.common.model.Gender;
import org.asasna.chat.common.model.User;
import org.asasna.chat.common.model.UserStatus;
import org.asasna.chat.server.model.db.DBConnection;


import javax.sql.RowSet;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {
    private RowSet rowSet = null;
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public UserDao() throws SQLException {
        conn = DBConnection.getConnection();
        statement = conn.createStatement();

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {

            String sql = "select * from users";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                users.add(user);

            }
            System.out.println(users.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        try {
            String sql = "select * from users where id = " + id;
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                user = extractUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUser(String phoneNumber, String password) {
        try {
            preparedStatement = conn.prepareStatement("select * from users where phone_number = ? and password = ?");
            preparedStatement.setString(1, phoneNumber);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return extractUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String phoneNumber) {
        try {
            preparedStatement = conn.prepareStatement("select * from users where phone_number = ?");
            preparedStatement.setString(1, phoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return extractUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateUser(int id, User user) {
        try {
            // You Must Add All Updated and Not Updated User Info In User Object
            String sql = "update users set" +
                    " id = ?" +
                    " ,phone_number = ?" +
                    " ,name = ?" +
                    " ,email = ?" +
                    " ,picture_path = ?" +
                    " ,password = ?" +
                    " ,gender = ?" +
                    " ,country = ?" +
                    " ,bio = ?" +
                    " ,date_of_birth = ?" +
                    " ,verified = ?" +
                    " ,chatbotoption = ?" +
                    " ,status_id = ?" +
                    " where id = " + id;
            preparedStatement = conn.prepareStatement(sql);
            injectUser(user);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0)
                return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        try {
            String sql = "delete from users where id = " + id;
            int affectedRows = statement.executeUpdate(sql);
            if (affectedRows > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(User user) {

        if (getUser(user.getId()) == null) {
            try {
                String sql = "insert into users values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                injectUser(user);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0)
                    return true;
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public List<User> getFriendList(User user) {
        List<User> friends = new ArrayList<>();
        try {
            String sql = "select second_member from contacts where first_member = ? " +
                    " union select first_member from contacts where second_number = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friends.add(getUser(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    @Override
    public ObservableList<PieChart.Data> getUsersByGender() {
        int females = 0;
        int males = 0;
        ResultSet resultSet;
        ObservableList<PieChart.Data> genderData = FXCollections.observableArrayList();
        String femaleSql = "select count(gender) from contacts where gender =" + Gender.Female + ")";
        String maleSql = "select count(gender) from contacts where gender =" + Gender.Male + ")";
        try {
            resultSet = statement.executeQuery(femaleSql);
            females = resultSet.getInt(1);
            resultSet = statement.executeQuery(maleSql);
            males = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PieChart.Data malesData = new PieChart.Data("Males", males);
        PieChart.Data feMalesData = new PieChart.Data("Females", females);
        genderData.addAll(malesData, feMalesData);
        return genderData;
    }

    @Override
    public ObservableList<PieChart.Data> getUsersByCountry() {
        return null;
        // need to ask for number of countries and what are they ?
    }

    @Override
    public ObservableList<PieChart.Data> getUsersByStatus() {
        int online = 0;
        int offline = 0;
        ResultSet resultSet;
        ObservableList<PieChart.Data> statusData = FXCollections.observableArrayList();
        String onlineSql = "select count(status) from contacts where status =" + UserStatus.ONLINE + ")";
        String offlineSql = "select count(status) from contacts where status <>" + UserStatus.ONLINE + ")";
        try {
            resultSet = statement.executeQuery(onlineSql);
            online = resultSet.getInt(1);
            resultSet = statement.executeQuery(offlineSql);
            offline = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PieChart.Data onlineData = new PieChart.Data("ONline", online);
        PieChart.Data offlineData = new PieChart.Data("OFFline", offline);
        statusData.addAll(onlineData, offlineData);
        return statusData;
    }

    @Override
    public boolean setNotification(int fromUserId, int toUserId) {

        try {
            String sql = "select * from invitations where from_id = ? and to_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, toUserId);
            preparedStatement.setInt(2,fromUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                resultSet.deleteRow();
                sql = "insert into contacts values(?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, fromUserId);
                preparedStatement.setInt(2, toUserId);
                int effectedRows = preparedStatement.executeUpdate();
                return false;
            }else{
                sql = "insert into invitations values(?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, fromUserId);
                preparedStatement.setInt(2, toUserId);
                int effectedRows = preparedStatement.executeUpdate();
                if(effectedRows == 1){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User extractUser(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setPhone(resultSet.getString(2));
            user.setName(resultSet.getString(3));
            user.setEmail(resultSet.getString(4));
            user.setImageURL(resultSet.getString(5));
            user.setPassword(resultSet.getString(6));
            Gender gender = Gender.Male;
            switch (resultSet.getString(7)) {
                case "Female":
                    gender = Gender.Female;
                    break;
            }
            user.setGender(gender);
            user.setCountry(resultSet.getString(8));
            user.setBio(resultSet.getString(9));
            user.setDateOfBirth(resultSet.getDate(10));
            user.setVerified(resultSet.getBoolean(11));
            user.setChatbotOption(resultSet.getBoolean(12));
            UserStatus status = UserStatus.OFFLINE;
            switch ((resultSet.getInt(13))) {
                case 1:
                    status = UserStatus.ONLINE;
                    break;
                case 2:
                    status = UserStatus.BUSY;
                    break;
            }
            user.setStatus(status);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    private void injectUser(User user) {
        try {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getImageURL());
            preparedStatement.setString(6, user.getPassword());
            Gender gender = user.getGender();
            String genderString = "Male";
            if (gender != null) {
                switch (gender) {
                    case Female:
                        genderString = "Female";
                        break;
                }
            }
            preparedStatement.setString(7, genderString);
            preparedStatement.setString(8, user.getCountry());
            preparedStatement.setString(9, user.getBio());
            // Convert Java.util.Date To Java.Sql.Date
//                preparedStatement.setDate(10, java.sql.Date.from(user.getDateOfBirth().toInstant()));
            preparedStatement.setDate(10, null);
            preparedStatement.setBoolean(11, user.isVerified());
            preparedStatement.setBoolean(12, user.isChatbotOption());
            UserStatus status = user.getStatus();
            int statusNumber = 0;
            if (status != null) {
                switch (status) {
                    case ONLINE:
                        statusNumber = 1;
                        break;
                    case BUSY:
                        statusNumber = 2;
                        break;
                }
            }
            preparedStatement.setInt(13, statusNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
