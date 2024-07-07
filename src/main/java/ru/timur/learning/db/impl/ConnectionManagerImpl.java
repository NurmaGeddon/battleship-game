package ru.timur.learning.db.impl;

import com.sun.tools.javac.Main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import javax.sql.DataSource;
import ru.timur.learning.db.ConnectionManager;

public class ConnectionManagerImpl implements ConnectionManager {

    private static final DataSource dataSource = configHikariDataSource(configProperties());

    private static Properties configProperties() {
        Properties dbProperties = new Properties();

        try {
            FileReader reader = new FileReader("src/main/resources/db.properties");
            dbProperties.load(reader);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return dbProperties;
    }

    private static DataSource configHikariDataSource(Properties dbProperties) {
        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setPassword(dbProperties.getProperty("db.password"));
        dataSource.setUsername(dbProperties.getProperty("db.username"));
        dataSource.setJdbcUrl(dbProperties.getProperty("db.url"));
        dataSource.setMaximumPoolSize(
                Integer.parseInt(
                        dbProperties.getProperty("db.hikari.MaxPoolSize")));
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}