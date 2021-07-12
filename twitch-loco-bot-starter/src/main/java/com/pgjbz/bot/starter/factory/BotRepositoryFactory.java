package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.BotRepository;
import com.pgjbz.bot.starter.repository.impl.BotRepositoryImpl;

public class BotRepositoryFactory extends AbstractBotRepositoryFactory{

    @Override
    public BotRepository createBotRepository() {
        var jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        return new BotRepositoryImpl(jdbcTemplate);
    }
}
