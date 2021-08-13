package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.*;
import com.pgjbz.bot.starter.repository.impl.*;

public class RepositoryFactory extends AbstractRepositoryFactory{

    RepositoryFactory(){}

    @Override
    public JokeRepository createJokeRepository() {
        return new JokeRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public StealRepository createStealRepository() {
        return new StealRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public FlirtRepository createFlirtRepository() {
        return new FlirtRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public BotResponseRepository createBotResponseRepository() {
        return new BotResponseRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public RankingRepository createRankingRepository() {
        return new RankingRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public CustomCommandRepository createCustomCommandRepository() {
        return new CustomCommandRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public MessageRepository createMessageRepository() {
        return new MessageRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public TokenRepository createTokenRepository() {
        return new TokenRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public IrcEventRepository createIrcEventRepository() {
        return new IrcEventRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public TwitchUserRepository createTwitchUserRepository() {
        return new TwitchUserRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

    @Override
    public BotRepository createBotRepository() {
        return new BotRepositoryImpl(AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate());
    }

}
