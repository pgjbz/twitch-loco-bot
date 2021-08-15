package com.pgjbz.bot.starter.listener.event;

import com.pgjbz.bot.starter.chain.AbstractIrcEventSaveChain;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class IrcEventSaveListener implements LocoIrcEventsListener {

    private final AbstractIrcEventSaveChain abstractIrcEventSaveChain;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenEvent(IrcEvent event) {
        executorService.submit(() -> abstractIrcEventSaveChain.doSaveIrcEvent(event));
    }

}
