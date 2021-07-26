package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TwitchUserRepository twitchUserRepository;

    @Override
    public boolean saveIfNotExists(String username) {
        log.info("Searching user {}", username);
        Optional<TwitchUser> optionalTwitchUser = findByUsername(username);
        if (optionalTwitchUser.isEmpty()) {
            log.info("Saving new user {}", username);
            return insert(new TwitchUser(username));
        }
        return false;
    }

    @Override
    public Optional<TwitchUser> findByUsername(String username) {
        return twitchUserRepository.findByUsername(username);
    }

    @Override
    public boolean insert(TwitchUser twitchUser) {
        return twitchUserRepository.insert(twitchUser);
    }

    @Override
    public List<TwitchUser> findAll() {
        return twitchUserRepository.findAll();
    }
}
