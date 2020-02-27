package org.asasna.chat.server.model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import org.asasna.chat.common.model.*;
import org.asasna.chat.server.model.db.DBConnection;
import org.asasna.chat.server.view.PasswordAuthentication;


import javax.imageio.ImageIO;
import javax.sql.RowSet;
import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao implements IUserDao {
    private RowSet rowSet = null;
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public UserDao() throws SQLException {
        conn = DBConnection.getConnection();
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////status
        //statement = conn.createStatement();
        statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

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
    public Map<Boolean, List<User>> getNonContactUsers(int meUserId) {
        List<User> users = new ArrayList<>();
        Map<Boolean, List<User>> map = new HashMap<>();
        try {
            String sql = "select * from users\n" +
                    "join invitations\n" +
                    "on users.id = invitations.to_id \n" +
                    "where invitations.from_id = " + meUserId;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                users.add(user);
            }
            map.put(true, users);
            users = new ArrayList<>();
            sql = "select * from users\n" +
                    "where users.id not in (\n" +
                    "select users.id from users\n" +
                    "join contacts\n" +
                    "on (users.id = contacts.first_member and contacts.second_member =  "+ meUserId + ")\n" +
                    "or (users.id = contacts.second_member and contacts.first_member =  "+ meUserId + ")\n" +
                    "and users.id <> " + meUserId +" )\n" +
                    "and users.id <> "+ meUserId + ";\n";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                if(!(map.get(true).stream().anyMatch(userItem -> userItem.getId() == user.getId()))){
                    users.add(user);
                }
            }
            map.put(false, users);
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
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
            if (resultSet.next()) {
                User user =  extractUser(resultSet);
//                PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
//                boolean found = passwordAuthentication.authenticate(password, user.getPassword());
//                if(found) return user;
                //will remove comments later
                return user;
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
            if (resultSet.next()) {
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
                    " union select first_member from contacts where second_member= ?";
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
        ResultSet resultSet ;
        ObservableList<PieChart.Data> genderData = FXCollections.observableArrayList();
        try {
            preparedStatement = conn.prepareStatement("select count(gender) from users where gender = ?");
            preparedStatement.setString(1, "Female");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            females = resultSet.getInt(1);
            preparedStatement.setString(1, "Male");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
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
        ResultSet resultSet ;
        ObservableList<PieChart.Data> countriesData = FXCollections.observableArrayList();
        String query = "SELECT count(country),country FROM users group by country" ;
        try {
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                PieChart.Data countryData = new PieChart.Data(resultSet.getString(2), resultSet.getInt(1));
                countriesData.add(countryData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countriesData;
    }

    @Override
    public ObservableList<PieChart.Data> getUsersByStatus() {
        int online = 0;
        int offline = 0;
        ResultSet resultSet ;
        ObservableList<PieChart.Data> statusData = FXCollections.observableArrayList();
        String onlineSql = "select count(status_id) from users where status_id =1" ;
        String offlineSql = "select count(status_id) from users where status_id <> 1"  ;
        try {
            resultSet = statement.executeQuery(onlineSql);
            resultSet.next();
            online = resultSet.getInt(1);
            resultSet = statement.executeQuery(offlineSql);
            resultSet.next();
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
            preparedStatement.setInt(2, fromUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.deleteRow();
                sql = "insert into contacts values(?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, fromUserId);
                preparedStatement.setInt(2, toUserId);
                int effectedRows = preparedStatement.executeUpdate();
                return false;
            } else {
                sql = "insert into invitations values(?, ?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, fromUserId);
                preparedStatement.setInt(2, toUserId);
                int effectedRows = preparedStatement.executeUpdate();
                if (effectedRows == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelNotification(int fromUserId, int toUserId) {

        try {
            String sql = "select * from invitations where from_id = ? and to_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setInt(1, fromUserId);
            preparedStatement.setInt(2, toUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                sql = "delete from invitations\n" +
                        "where from_id = ? and to_id = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, fromUserId);
                preparedStatement.setInt(2, toUserId);
                int effectedRows = preparedStatement.executeUpdate();
                System.out.println(effectedRows);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Notification> getNotification(int id) {
        List<Notification> notifications = new ArrayList<>();
        try {
            String sql = "select * from users\n" +
                    "join invitations\n" +
                    "on users.id = invitations.from_id\n" +
                    "where invitations.to_id = " + id;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                notifications.add(new Notification(NotificationType.FRIEND_REQUEST, user));
            }
            return notifications;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;


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
            System.out.println(user.getImageURL());
            user.setImage(new Image(getClass().getResource(user.getImageURL()).toExternalForm()));
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
                case 3:
                    status = UserStatus.AWAY;
                    break;
            }
            user.setStatus(status);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    private void injectUser(User user) {
        System.out.println(user.getPhone());
        try {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            File file = new File("Server\\src\\main\\resources\\org\\asasna\\chat\\server\\"+user.getPhone()+".jpg");
            file.mkdirs();//nehal edit
            ImageIO.write(SwingFXUtils.fromFXImage(user.getImage(), null), "jpg", file);
            preparedStatement.setString(5, user.getPhone()+".jpg");
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
                    case AWAY:
                        statusNumber = 3;
                        break;
                }
            }
            preparedStatement.setInt(13, statusNumber);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateUserStatues(int id, UserStatus status) {
        ResultSet resultSet;
        String Sql = "select * from users  where id =" + id;
        try {
            resultSet = statement.executeQuery(Sql);
            resultSet.beforeFirst();
            int statusNumber = 0;
            while (resultSet.next()) {
                switch (status) {
                    case ONLINE:
                        statusNumber = 1;
                        break;
                    case BUSY:
                        statusNumber = 2;
                        break;
                    case AWAY:
                        statusNumber = 3;
                        break;
                }
                resultSet.updateInt(13, statusNumber);
                resultSet.updateRow();


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
