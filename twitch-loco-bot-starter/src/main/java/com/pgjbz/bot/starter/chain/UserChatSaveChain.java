package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class UserChatSaveChain extends AbstractChatSaveChain {

    private final TwitchUserRepository twitchUserRepository;

    @Override
    public void doChatSave(ChatMessage chatMessage) {

        String username = chatMessage.getUser();
        log.info("Searching user {}", username);

        try {
            Optional<TwitchUser> optionalTwitchUser = twitchUserRepository.findByUsername(username);
            if (optionalTwitchUser.isEmpty()) {
                log.info("Saving new user {}", username);
                twitchUserRepository.insert(new TwitchUser(username));
            }
        } catch (Exception e) {
            log.error("Error on save user {}", username, e);
            return;
        }

        if(nonNull(next))
            next.doChatSave(chatMessage);
    }

}
