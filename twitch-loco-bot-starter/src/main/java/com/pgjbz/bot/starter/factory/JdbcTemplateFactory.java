package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.impl.JdbcTemplateImpl;

public class JdbcTemplateFactory extends AbstractJdbcTemplateFactory{

    @Override
    public JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplateImpl();
    }
}
