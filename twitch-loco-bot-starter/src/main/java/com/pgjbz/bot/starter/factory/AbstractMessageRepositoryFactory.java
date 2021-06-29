package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.MessageRepository;

import static java.util.Objects.isNull;

public abstract class AbstractMessageRepositoryFactory {

    private static AbstractMessageRepositoryFactory instance;

    public abstract MessageRepository createMessageRepository();

    public static AbstractMessageRepositoryFactory getInstance() {
        if(isNull(instance))
            instance = new MessageRepositoryFactory();
        return instance;
    }
}
