package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class IrcEventUserCheckChain extends AbstractIrcEventSaveChain{

    private final UserService userService;

    @Override
    public void doSaveIrcEvent(IrcEvent ircEvent) {

        String username = ircEvent.getUsername();
        if(BotUtils.isBot(username)) {
            log.info("Event from bot {}... skipping", username);
            return;
        }

        log.info("Perform user validation for user {}", username);
        try {
            userService.saveIfNotExists(username);
        } catch (Exception e) {
            log.error("Error on save user {}", username, e);
            return;
        }

        if(nonNull(nextStep))
            nextStep.doSaveIrcEvent(ircEvent);
    }
}
