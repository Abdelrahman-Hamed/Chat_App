package org.asasna.chat.server.model.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;

public class MyDataSourceFactory {
    public static DataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream input = null;
        MysqlDataSource mySqlDataSource = null;
        String filePath = "./Server/src/main/resources/org/asasna/chat/server/DB_Properties.properties";
        try {
            input = new FileInputStream(filePath);
            props.load(input);
            mySqlDataSource = new MysqlDataSource();
            mySqlDataSource.setURL(props.getProperty("MYSQL_DB_URL"));
            mySqlDataSource.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mySqlDataSource.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mySqlDataSource;
    }
}
