package com.pgjbz.bot.starter.database.pool;

import com.pgjbz.bot.starter.configs.Configuration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import static java.util.Objects.nonNull;

@Log4j2
public class BotDatabaseConnection implements Closeable {

    private Connection connection;
    @Getter
    private int connections = 0;
    @Getter
    private boolean closed = false;
    private Date connectionOpen;

    @Override
    public void close() throws IOException {
        if(connections == 0 && nonNull(connectionOpen) && (connectionOpen.getTime() - new Date().getTime() < 10000))
            try {
                connection.close();
                closed = true;
            } catch (SQLException e) {
                log.error("Error on close connection", e);
            }
        else
            decreaseConnection();
    }

    private void increaseConnections() {
        synchronized (this) {
            connections++;
        }
    }

    private void decreaseConnection() {
        synchronized (this) {
            connections--;
        }
    }

    public Connection getConnection() throws SQLException {
        Map<String, String> configurations = Configuration.getConfigs();
        try {
            if(nonNull(connection) && !connection.isClosed()) {
                increaseConnections();
                return connection;
            }
            String username = configurations.get("DATABASE_USER");
            String password = configurations.get("DATABASE_PASSWORD");
            String host = configurations.get("DATABASE_HOST");
            String port = configurations.get("DATABASE_PORT");
            String databaseName = configurations.get("DATABASE_NAME");
            String url  = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
            connection = DriverManager.getConnection(url, username, password);
            increaseConnections();
            connectionOpen = new Date();
        }catch (SQLException e) {
            log.error("Error on open connection", e);
        }
        return connection;
    }
}
