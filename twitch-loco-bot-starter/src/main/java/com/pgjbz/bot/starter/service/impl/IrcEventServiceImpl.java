package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.repository.IrcEventRepository;
import com.pgjbz.bot.starter.service.IrcEventService;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class IrcEventServiceImpl implements IrcEventService {

    private final IrcEventRepository ircEventRepository;

    @Override
    public boolean insert(IrcEvent ircEvent) {
        boolean inserted = false;
        try {
            inserted = ircEventRepository.insert(ircEvent);
        } catch (Exception e) {
            log.error("Error on insert new irc event {}", ircEvent.toString(), e);
        }
        return inserted;
    }
}
