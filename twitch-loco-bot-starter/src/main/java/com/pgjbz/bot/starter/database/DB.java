package com.pgjbz.bot.starter.database;

import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.database.pool.BotDatabaseConnection;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class DB {

    private static final int MAX_CONN = 5;

    private static final List<BotDatabaseConnection> pool = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger index = new AtomicInteger(0);
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    static {
        executorService.scheduleAtFixedRate(() -> {
                    log.debug("Starting pool check... pool size: {}", pool.size());
                    for(BotDatabaseConnection botDatabaseConnection: pool)
                        try {
                            botDatabaseConnection.close();
                            if(botDatabaseConnection.isClosed())
                                pool.remove(botDatabaseConnection);
                            index.set(0);
                        } catch (IOException e) {
                            log.error("Error on close connection", e);
                        }
                }
                ,
                0,
                30,
                TimeUnit.SECONDS);
    }

    private static void increaseIndex() {
        index.incrementAndGet();
    }

    public static BotDatabaseConnection getConnection() {
        log.debug("Getting connection from the pool");
        BotDatabaseConnection botDatabaseConnection = null;
        increaseIndex();
        if (index.get() >= MAX_CONN || index.get() >= pool.size())
            index.set(0);
        int actualIndex = index.get();
        if (pool.size() < MAX_CONN) {
            botDatabaseConnection = new BotDatabaseConnection();
            pool.add(botDatabaseConnection);
        } else
            botDatabaseConnection = pool.get(actualIndex);
        log.debug("Connection size {}, index {}", botDatabaseConnection.getConnections(), actualIndex);
        return botDatabaseConnection;

    }

    public static void flywayStart(){
        Map<String, String> configurations = databaseConfigs();
        String username = configurations.get("username");
        String password = configurations.get("password");
        String host = configurations.get("host");
        String port = configurations.get("port");
        String databaseName = configurations.get("databaseName");
        String url  = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
        Flyway flyway = Flyway.configure().dataSource(url, username, password).load();
        flyway.migrate();
    }

    private static Map<String, String> databaseConfigs() {
        Map<String, String> properties = Configuration.getConfigs();
        Map<String, String> configurations = new HashMap<>();
        configurations.put("username", properties.get("DATABASE_USER"));
        configurations.put("password", properties.get("DATABASE_PASSWORD"));
        configurations.put("host", properties.get("DATABASE_HOST"));
        configurations.put("databaseName", properties.get("DATABASE_NAME"));
        configurations.put("port", properties.get("DATABASE_PORT"));
        return configurations;
    }
}
