package com.pgjbz.bot.starter.database;

import com.pgjbz.bot.starter.configs.Configuration;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.logging.Level;

import static com.pgjbz.bot.starter.configs.BotConstants.CONFIG_FILE_SYSTEM_PROPERTY;

@Log
public class DB {

    public static Connection getConnection() {
        Map<String, String> configurations = Configuration.getConfigs(System.getProperty(CONFIG_FILE_SYSTEM_PROPERTY));
        Connection connection = null;
        try {
            String username = configurations.get("DATABASE_USER");
            String password = configurations.get("DATABASE_PASSWORD");
            String host = configurations.get("DATABASE_HOST");
            String port = configurations.get("DATABASE_PORT");
            String databaseName = configurations.get("DATABASE_NAME");
            String url  = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e){
            log.log(Level.SEVERE, "Error on connect: " + e.getMessage(), e);
        }
        return connection;
    }
}
