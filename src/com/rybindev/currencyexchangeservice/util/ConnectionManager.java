package com.rybindev.currencyexchangeservice.util;

import lombok.experimental.UtilityClass;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    private static final String URL;
    private static final SQLiteConnectionPoolDataSource dataSource;

    static {
        loadDriver();
        URL = getUrl();
        dataSource = getDataSource();
    }

    public static Connection getConnection() {
        try {
            return dataSource.getPooledConnection().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static SQLiteConnectionPoolDataSource getDataSource() {
        SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
        dataSource.setUrl(URL);
        return dataSource;
    }

    private static String getUrl() {
        return "jdbc:sqlite:" + ConnectionManager.class.getClassLoader().getResource("db/currency.sqlite").getPath();
    }

    private static void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
