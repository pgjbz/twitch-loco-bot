package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.repository.impl.TwitchUserRepositoryImpl;

public class TwitchUserRepositoryFactory extends AbstractTwitchUserRepositoryFactory{

    @Override
    public TwitchUserRepository createTwitchUserRepository() {
        var jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        return new TwitchUserRepositoryImpl(jdbcTemplate);
    }
}
