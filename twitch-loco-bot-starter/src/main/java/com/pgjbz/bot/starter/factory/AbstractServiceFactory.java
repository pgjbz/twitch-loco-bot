package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.service.*;

import static java.util.Objects.isNull;

public abstract class AbstractServiceFactory {

    private static AbstractServiceFactory instance;

    public static AbstractServiceFactory getInstance() {
        if(isNull(instance))
            instance = new ServiceFactory();
        return instance;
    }

    public abstract UserService createUserService();
    public abstract BotService createBotService();
    public abstract FlirtService createFlirtService();
    public abstract JokeService createJokeService();
    public abstract MessageService createMessageService();
    public abstract IrcEventService createIrcEventService();
    public abstract StealService createStealService();
    public abstract TokenService createTokenService();
    public abstract CustomCommandService createCustomCommandService();
    public abstract RankingService createRankingService();
}
