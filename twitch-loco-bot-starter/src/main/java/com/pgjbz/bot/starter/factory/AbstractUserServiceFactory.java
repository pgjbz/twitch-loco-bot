package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.service.UserService;

import static java.util.Objects.isNull;

public abstract class AbstractUserServiceFactory {

    private static AbstractUserServiceFactory instance;

    public static AbstractUserServiceFactory getInstance() {
        if(isNull(instance))
            instance = new UserServiceFactory();
        return instance;
    }

    public abstract UserService createUserService();
}
