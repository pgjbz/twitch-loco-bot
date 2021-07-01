package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class UserCheckTokenChain extends AbstractTokenChain{

    private final TwitchUserRepository twitchUserRepository;

    @Override
    public void doAddUnits(Token token) {
        String username = token.getPk().getUsername();
        try {
            log.info("Checking user {}", username);
            if(twitchUserRepository.findByUsername(username).isEmpty()) {
                log.info("Saving new user {}", username);
                twitchUserRepository.insert(new TwitchUser(username));
            }
        } catch (Exception e) {
            log.error("Error on check user {}", username, e);
            return;
        }
        if(nonNull(next))
            next.doAddUnits(token);
    }
}
