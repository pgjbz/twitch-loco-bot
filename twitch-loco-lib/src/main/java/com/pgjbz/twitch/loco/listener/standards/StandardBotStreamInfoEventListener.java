package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.StreamInfo;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class StandardBotStreamInfoEventListener implements BotStreamInfoEventListener {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*
       Standard Bot stream info listener only log info
    */
    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        executorService.submit(() -> log.info(streamInfo.toString()));
    }

}
