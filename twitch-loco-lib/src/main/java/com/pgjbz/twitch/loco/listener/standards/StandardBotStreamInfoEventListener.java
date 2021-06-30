package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.StreamInfo;

public class StandardBotStreamInfoEventListener implements BotStreamInfoEventListener {

    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        System.out.println(streamInfo.toString());
    }

}
