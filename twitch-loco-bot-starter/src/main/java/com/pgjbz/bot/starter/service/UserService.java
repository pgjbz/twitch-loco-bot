package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.TwitchUser;

import java.util.Optional;

public interface UserService extends Service<TwitchUser, Long> {

    boolean saveIfNotExists(String username);
    Optional<TwitchUser> findByUsername(String username);

}
