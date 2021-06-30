package com.pgjbz.twitch.loco.thread;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.service.StreamInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class BotStreamInfoThread implements Runnable {

    private final StreamInfoService streamInfoService;
    private final TwitchLoco twitchLoco;
    private final List<BotStreamInfoEventListener> listeners;

    @Override
    public void run() {
        log.info("Realizing request");
        streamInfoService.getStreamInfo(twitchLoco.getChannel())
                .ifPresentOrElse(
                        streamInfo -> listeners.forEach(listener -> listener.listenBotEvent(streamInfo)
                        ),
                        () -> log.info("No response")
                );
    }

}
