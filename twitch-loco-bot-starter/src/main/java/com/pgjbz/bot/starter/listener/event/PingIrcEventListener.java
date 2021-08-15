package com.pgjbz.bot.starter.listener.event;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.enums.CommandSend;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class PingIrcEventListener implements LocoIrcEventsListener {

    private final TwitchConnection twitchConnection;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenEvent(IrcEvent event) {
        if (isNull(event) || event.getCommandReceive() != CommandReceive.PING) return;
        executorService.submit(() -> {
            log.info("Ping command received send PONG!");
            twitchConnection.sendCommand(CommandSend.PONG);
        });
    }
}
