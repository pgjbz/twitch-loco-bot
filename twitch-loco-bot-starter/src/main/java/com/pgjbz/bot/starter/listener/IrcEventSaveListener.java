package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.chain.AbstractIrcEventSaveChain;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IrcEventSaveListener implements LocoIrcEventsListener {

    private final AbstractIrcEventSaveChain abstractIrcEventSaveChain;

    @Override
    public void listenEvent(IrcEvent event) {
        abstractIrcEventSaveChain.doSaveIrcEvent(event);
    }

}
