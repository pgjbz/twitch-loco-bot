package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

@Log4j2
public class StandardLocoIrcEventsListener implements LocoIrcEventsListener {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*
      Standard Bot stream info listener only log irc event
   */
    @Override
    public void listenEvent(IrcEvent event) {
        if(nonNull(event))
            executorService.submit(() -> executorService.submit(() ->log.info(event)));
    }
}
