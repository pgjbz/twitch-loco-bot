package com.pgjbz.bot.starter.listener;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@RequiredArgsConstructor
public class UserNoticeIrcEventListener implements LocoIrcEventsListener {

    private static final String KEY_TO_VERIFY_SUB = "msg-id";

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final TwitchConnection twitchConnection;

    @Override
    public void listenEvent(IrcEvent event) {
        if(event.getCommandReceive() != CommandReceive.USERNOTICE) return;
        executorService.submit(() -> executeSubEventValidation(event));
    }

    private void executeSubEventValidation(IrcEvent event) {
        log.info("User notice receive performing validation");
        Map<String, String> eventKeys = event.getKeys();
        if(eventKeys.containsKey(KEY_TO_VERIFY_SUB) && eventKeys.get(KEY_TO_VERIFY_SUB).contains("sub")) {
            log.info("Sub event, sending message");
            String message = eventKeys.get("system-msg").replace("\\s", " ") +
                    " GOD <3";
            twitchConnection.sendMessage(message);
        }
    }
}
