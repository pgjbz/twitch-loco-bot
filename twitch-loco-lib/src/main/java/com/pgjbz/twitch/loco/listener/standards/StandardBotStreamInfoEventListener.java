package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.StreamInfo;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StandardBotStreamInfoEventListener implements BotStreamInfoEventListener {

    /*
       Standard Bot stream info listener only log info
    */
    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        log.info(streamInfo.toString());
    }

}
