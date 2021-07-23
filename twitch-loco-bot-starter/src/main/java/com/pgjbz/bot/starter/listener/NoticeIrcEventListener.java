package com.pgjbz.bot.starter.listener;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class NoticeIrcEventListener implements LocoIrcEventsListener {

    private final TwitchConnection twitchConnection;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenEvent(IrcEvent event) {
        if (event.getCommandReceive() != CommandReceive.NOTICE) return;
        executorService.submit(() -> {
            executeNoticeEvent(event);
        });
    }

    private void executeNoticeEvent(IrcEvent event) {
        log.info("Perform notice event");
        String eventString = event.getUnknownString();
        if (eventString.contains("@msg-id=room_mods") && retrieveAndUpdateModList(eventString)
                .contains(twitchConnection.getBotName())) {
            log.info("Bot is moderator set message interval to 5s");
            twitchConnection.setMessageInterval(5000L);
        } else {
            log.info("Bot is not moderator set message interval to 30s");
            twitchConnection.setMessageInterval(30000L);
        }
    }

    private List<String> retrieveAndUpdateModList(String eventString) {
        log.info("Updating mod list");
        List<String> mods = Stream.of(eventString.substring(eventString.indexOf("are: ") + 5).split(", "))
                .collect(Collectors.toList());
        mods.forEach(twitchConnection::addMod);
        return mods;
    }
}
