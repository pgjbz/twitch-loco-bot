package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TwitchUserRepository twitchUserRepository;

    @Override
    public boolean saveIfNotExists(String username) {
        log.info("Searching user {}", username);
        Optional<TwitchUser> optionalTwitchUser = twitchUserRepository.findByUsername(username);
        if (optionalTwitchUser.isEmpty()) {
            log.info("Saving new user {}", username);
            return twitchUserRepository.insert(new TwitchUser(username));
        }
        return false;
    }
}
