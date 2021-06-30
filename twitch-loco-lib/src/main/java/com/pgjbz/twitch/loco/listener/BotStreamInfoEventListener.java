package com.pgjbz.twitch.loco.listener;

import com.pgjbz.twitch.loco.model.StreamInfo;

public interface BotStreamInfoEventListener {

    void listenBotEvent(StreamInfo streamInfo);

}
