package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.BotRepository;

import static java.util.Objects.isNull;

public abstract class AbstractBotRepositoryFactory {

    private static AbstractBotRepositoryFactory instance;

    public abstract BotRepository createBotRepository();

    public static AbstractBotRepositoryFactory getInstance() {
        if(isNull(instance))
            instance = new BotRepositoryFactory();
        return instance;
    }
}
