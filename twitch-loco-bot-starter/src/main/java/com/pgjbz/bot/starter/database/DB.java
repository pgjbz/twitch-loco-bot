package com.pgjbz.bot.starter.database;

import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.database.pool.BotDatabaseConnection;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class DB {

    private static final int MAX_CONN = 5;

    private static final List<BotDatabaseConnection> pool = Collections.synchronizedList(new ArrayList<>());
    private static Integer index = 0;
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    static {
        executorService.scheduleAtFixedRate(() -> {
                    log.info("Starting pool check... pool size: {}", pool.size());
                    for(BotDatabaseConnection botDatabaseConnection: pool)
                        try {
                            botDatabaseConnection.close();
                            if(botDatabaseConnection.isClosed())
                                pool.remove(botDatabaseConnection);
                            index = 0;
                        } catch (IOException e) {
                            log.error("Error on close connection", e);
                        }
                }
                ,
                1,
                30,
                TimeUnit.SECONDS);
    }

    private static void increaseIndex() {
        synchronized (index) {
            index++;
        }
    }

    public static BotDatabaseConnection getConnection() {
        log.info("Getting connection from the pool");

        BotDatabaseConnection botDatabaseConnection = null;
        increaseIndex();
        if (index >= MAX_CONN || index >= pool.size())
            index = 0;
        if (pool.size() < MAX_CONN) {
            botDatabaseConnection = new BotDatabaseConnection();
            pool.add(botDatabaseConnection);
        } else
            botDatabaseConnection = pool.get(index);
        log.info("Connection size {}, index {}", botDatabaseConnection.getConnections(), index);
        return botDatabaseConnection;

    }

    public static void flywayStart(){
        Map<String, String> configurations = Configuration.getConfigs();
        String username = configurations.get("DATABASE_USER");
        String password = configurations.get("DATABASE_PASSWORD");
        String host = configurations.get("DATABASE_HOST");
        String port = configurations.get("DATABASE_PORT");
        String databaseName = configurations.get("DATABASE_NAME");
        String url  = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
        Flyway flyway = Flyway.configure().dataSource(url, username, password).load();
        flyway.migrate();
    }
}
