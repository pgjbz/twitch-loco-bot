package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.IrcEventRepository;

import static java.util.Objects.isNull;

public abstract class AbstractIrcRepositoryFactory {

    private static AbstractIrcRepositoryFactory instance;

    public static AbstractIrcRepositoryFactory getIsntance() {
        if(isNull(instance))
            instance  = new IrcRepositoryFactory();
        return instance;
    }

    public abstract IrcEventRepository createIrcEventRepository();
}
