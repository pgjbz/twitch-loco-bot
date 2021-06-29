package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TwitchUserRepository;

import static java.util.Objects.isNull;

public abstract class AbstractTwitchUserRepositoryFactory {

    private static AbstractTwitchUserRepositoryFactory instance;

    public abstract TwitchUserRepository createTwitchUserRepository();

    public static AbstractTwitchUserRepositoryFactory getInstance() {
        if(isNull(instance))
            instance = new TwitchUserRepositoryFactory();
        return instance;
    }
}
