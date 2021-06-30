package com.pgjbz.twitch.loco.schedule;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.service.StreamInfoService;
import com.pgjbz.twitch.loco.thread.BotStreamInfoThread;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public class BotStreamInfoEventSchedule {

    private final StreamInfoService streamInfoService;
    private final TwitchLoco twitchLoco;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final List<BotStreamInfoEventListener> listeners = new CopyOnWriteArrayList<>();

    public void startSchedule(@NonNull Long minutes){
        log.info("Starting schedule with fixed rate {}", minutes);
        executorService.scheduleAtFixedRate(
                new BotStreamInfoThread(streamInfoService, twitchLoco, listeners),
                1,
                minutes,
                TimeUnit.MINUTES);
    }

    public void addBotStreamInfoEventListener(BotStreamInfoEventListener listener) {
        listeners.add(listener);
    }

}
