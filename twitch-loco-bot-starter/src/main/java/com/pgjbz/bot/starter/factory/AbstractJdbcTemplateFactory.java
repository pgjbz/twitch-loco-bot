package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;

import static java.util.Objects.isNull;

public abstract class AbstractJdbcTemplateFactory {

    private static AbstractJdbcTemplateFactory instance;

    public abstract JdbcTemplate createJdbcTemplate();

    public static AbstractJdbcTemplateFactory getInstance() {
        if(isNull(instance))
            instance = new JdbcTemplateFactory();
        return instance;
    }

}
