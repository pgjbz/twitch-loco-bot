package com.pgjbz.bot.starter.repository;

import com.pgjbz.bot.starter.model.TwitchUser;

import java.util.Optional;

public interface TwitchUserRepository extends Repository<TwitchUser, Long> {

    Optional<TwitchUser> findByUsername(String username);

}
