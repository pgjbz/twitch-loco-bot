package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.thread.UserJoinThread;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.RequiredArgsConstructor;

import static com.pgjbz.bot.starter.util.BotUtils.isBot;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class UserJoinListener implements LocoIrcEventsListener {

    private final UserJoinThread userJoinThread;

    @Override
    public void listenEvent(IrcEvent event) {
        if(nonNull(event) && !isBot(event.getUsername()))
            userJoinThread.addJoinEvent(event);
    }

}
