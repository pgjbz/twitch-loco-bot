package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TokenRepository;
import com.pgjbz.bot.starter.repository.impl.TokenRepositoryImpl;

public class TokenRepositoryFactory extends AbstractTokenRepositoryFactory{
    @Override
    public TokenRepository createTokenRepository() {
        var jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        return new TokenRepositoryImpl(jdbcTemplate);
    }
}
