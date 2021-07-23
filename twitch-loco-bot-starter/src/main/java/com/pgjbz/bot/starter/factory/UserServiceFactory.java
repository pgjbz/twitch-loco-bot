package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.bot.starter.service.impl.UserServiceImpl;

public class UserServiceFactory extends AbstractUserServiceFactory {

    UserServiceFactory(){}

    @Override
    public UserService createUserService() {
        TwitchUserRepository twitchUserRepository = AbstractRepositoryFactory.getInstance().createTwitchUserRepository();
        return new UserServiceImpl(twitchUserRepository);
    }

}
