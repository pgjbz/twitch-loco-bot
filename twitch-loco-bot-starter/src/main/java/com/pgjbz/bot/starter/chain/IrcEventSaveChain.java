package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.service.IrcEventService;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class IrcEventSaveChain extends AbstractIrcEventSaveChain{

    private final IrcEventService ircEventService;

    @Override
    public void doSaveIrcEvent(IrcEvent ircEvent) {
        log.info("Performing irc event save - {}", ircEvent.toString());
        try {
            ircEventService.insert(ircEvent);
        } catch (Exception e) {
            log.error("Error on save irc event - {}: {}", ircEvent.toString(), e.getMessage(), e);
            return;
        }
        if(nonNull(nextStep))
            nextStep.doSaveIrcEvent(ircEvent);
    }
}
