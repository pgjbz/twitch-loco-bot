package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TokenRepository;

import static java.util.Objects.isNull;

public abstract class AbstractTokenRepositoryFactory {

    private static AbstractTokenRepositoryFactory instance;

    public abstract TokenRepository createTokenRepository();

    public static AbstractTokenRepositoryFactory getInstance() {
        if(isNull(instance))
            instance = new TokenRepositoryFactory();
        return instance;
    }
}
