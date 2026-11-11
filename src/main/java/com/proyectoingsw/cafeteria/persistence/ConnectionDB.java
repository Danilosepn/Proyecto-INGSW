package com.proyectoingsw.cafeteria.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionDB {
    private final String url;
    private final String driver;
    private final String user;
    private final String password;
    private Connection connection;

    public ConnectionDB(String url, String user, String password) {
        this(url, null, user, password);
    }

    public ConnectionDB(String url, String driver, String user, String password) {
        this.url = url;
        this.driver = driver;
        this.user = user;
        this.password = password;
    }

    public Connection connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new SQLException("A database connection is already open.");
        }
        if (driver != null && !driver.isBlank()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException exception) {
                throw new SQLException("JDBC driver not found: " + driver, exception);
            }
        }
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
}
