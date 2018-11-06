package org.onap.vid.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class ConnectionFactory {

    public Connection getConnection(String url, String username, String password) throws SQLException {
        checkNotNull(url);
        checkNotNull(username);
        checkNotNull(password);
        return DriverManager.getConnection(url, username, password);
    }
}
