package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.MessageRepository;
import com.pgjbz.bot.starter.repository.impl.MessageRepositoryImpl;

public class MessageRepositoryFactory extends AbstractMessageRepositoryFactory {

    @Override
    public MessageRepository createMessageRepository() {
        var jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        return new MessageRepositoryImpl(jdbcTemplate);
    }
}
