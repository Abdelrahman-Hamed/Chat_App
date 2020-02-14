package org.asasna.chat.server.model.db;

import java.sql.Connection;

public interface DBConnectionManager {
    Connection getConnection();
}
