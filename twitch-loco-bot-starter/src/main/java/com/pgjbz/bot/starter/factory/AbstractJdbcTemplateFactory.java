package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;

public abstract class AbstractJdbcTemplateFactory {

    public abstract JdbcTemplate createJdbcTemplate();

    public static AbstractJdbcTemplateFactory getInstance() {
        return new JdbcTemplateFactory();
    }

}
