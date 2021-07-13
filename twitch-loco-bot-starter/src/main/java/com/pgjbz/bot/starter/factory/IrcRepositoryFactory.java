package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.IrcEventRepository;
import com.pgjbz.bot.starter.repository.impl.IrcEventRepositoryImpl;

public class IrcRepositoryFactory extends AbstractIrcRepositoryFactory {

    @Override
    public IrcEventRepository createIrcEventRepository() {
        var jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        return new IrcEventRepositoryImpl(jdbcTemplate);
    }
}
