package org.asasna.chat.server.model.dao;

import org.asasna.chat.server.model.db.DBConnection;

import javax.sql.RowSet;
import java.sql.*;

public class ContactDao implements IContactDao{
    private RowSet rowSet = null;
    private Connection conn = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    public ContactDao() throws SQLException {
        conn = DBConnection.getConnection();
        statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }
    @Override
    public boolean removeFriend(int meId, int friendId) {
        try {
            return statement.executeUpdate("delete from contacts where (first_member = "+ meId +" and second_member = "+ friendId+") or (first_member = "+ friendId +" and second_member = "+ meId +")") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
