package com.pgjbz.bot.starter.listener;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JoinChatListener implements LocoIrcEventsListener {

    private final TwitchConnection twitchConnection;

    @Override
    public void listenEvent(String event) {
        if(event.matches(".*366.*/NAMES\\slist"))
            twitchConnection.sendMessage("UOOOUU");
    }
}
