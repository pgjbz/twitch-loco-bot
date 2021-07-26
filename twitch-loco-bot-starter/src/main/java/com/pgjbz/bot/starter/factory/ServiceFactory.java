package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.*;
import com.pgjbz.bot.starter.service.*;
import com.pgjbz.bot.starter.service.impl.*;

public class ServiceFactory extends AbstractServiceFactory {

    ServiceFactory(){}

    @Override
    public UserService createUserService() {
        TwitchUserRepository twitchUserRepository = AbstractRepositoryFactory.getInstance().createTwitchUserRepository();
        return new UserServiceImpl(twitchUserRepository);
    }

    @Override
    public BotService createBotService() {
        BotRepository botRepository = AbstractRepositoryFactory.getInstance().createBotRepository();
        return new BotServiceImpl(botRepository);
    }

    @Override
    public FlirtService createFlirtService() {
        FlirtRepository flirtRepository = AbstractRepositoryFactory.getInstance().createFlirtRepository();
        return new FlirtServiceImpl(flirtRepository);
    }

    @Override
    public JokeService createJokeService() {
        JokeRepository jokeRepository = AbstractRepositoryFactory.getInstance().createJokeRepository();
        return new JokeServiceImpl(jokeRepository);
    }

    @Override
    public MessageService createMessageService() {
        MessageRepository messageRepository = AbstractRepositoryFactory.getInstance().createMessageRepository();
        return new MessageServiceImpl(messageRepository);
    }

    @Override
    public IrcEventService createIrcEventService() {
        IrcEventRepository ircEventRepository = AbstractRepositoryFactory.getInstance().createIrcEventRepository();
        return new IrcEventServiceImpl(ircEventRepository);
    }

    @Override
    public StealService createStealService() {
        StealRepository stealRepository = AbstractRepositoryFactory.getInstance().createStealRepository();
        return new StealServiceImpl(stealRepository);
    }

    @Override
    public TokenService createTokenService() {
        TokenRepository tokenRepository = AbstractRepositoryFactory.getInstance().createTokenRepository();
        return new TokenServiceImpl(tokenRepository);
    }

}
