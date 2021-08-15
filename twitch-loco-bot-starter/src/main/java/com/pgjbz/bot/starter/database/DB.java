package com.pgjbz.bot.starter.database;

import com.pgjbz.bot.starter.configs.Configuration;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

@Log4j2
public class DB {

    public static Connection getConnection() {
        Map<String, String> configurations = Configuration.getConfigs();
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
            log.error("Error on connect: {}", e.getMessage(), e);
        }
        return connection;
    }
}
