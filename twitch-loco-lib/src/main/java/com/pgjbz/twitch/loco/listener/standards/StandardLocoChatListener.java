package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

@Log4j2
public class StandardLocoChatListener implements LocoChatListener {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*
        Standard Chat listener only log chat
     */
    @Override
    public void listenChat(ChatMessage message) {
        if(nonNull(message))
           executorService.submit(() -> log.info(message.getUser() + " on " + message.getChannel() + " -> " + message.getMessage()));
    }
}
