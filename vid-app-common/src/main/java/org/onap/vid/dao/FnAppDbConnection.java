package org.onap.vid.dao;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class FnAppDbConnection {

    public Connection getConnection(String url, String username, String password) throws SQLException {
        return credentialsExist(url, username, password) ? DriverManager.getConnection(url, username, password) : null;
    }

    private static boolean credentialsExist(String url, String username, String password) {
        return url != null && username != null && password != null;
    }
}
