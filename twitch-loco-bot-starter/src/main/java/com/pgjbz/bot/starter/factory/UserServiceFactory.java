package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.bot.starter.service.impl.UserServiceImpl;

public class UserServiceFactory extends AbstractUserServiceFactory {

    @Override
    public UserService createUserService() {
        TwitchUserRepository twitchUserRepository = AbstractTwitchUserRepositoryFactory.getInstance().createTwitchUserRepository();
        return new UserServiceImpl(twitchUserRepository);
    }

}
