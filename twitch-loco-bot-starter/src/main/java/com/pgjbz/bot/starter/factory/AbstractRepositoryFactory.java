package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.*;

import static java.util.Objects.isNull;

public abstract class AbstractRepositoryFactory {

    private static AbstractRepositoryFactory instance;

    public abstract IrcEventRepository createIrcEventRepository();
    public abstract MessageRepository createMessageRepository();
    public abstract TokenRepository createTokenRepository();
    public abstract TwitchUserRepository createTwitchUserRepository();
    public abstract BotRepository createBotRepository();
    public abstract JokeRepository createJokeRepository();
    public abstract StealRepository createStealRepository();
    public abstract FlirtRepository createTeaserRepository();

    public static AbstractRepositoryFactory getInstance() {
        if(isNull(instance))
            instance = new RepositoryFactory();
        return instance;
    }
}
