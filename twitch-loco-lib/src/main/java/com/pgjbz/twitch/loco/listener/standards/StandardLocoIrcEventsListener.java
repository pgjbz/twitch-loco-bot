package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StandardLocoIrcEventsListener implements LocoIrcEventsListener {


    /*
      Standard Bot stream info listener only log irc event
   */
    @Override
    public void listenEvent(String event) {
        log.info(event);
    }
}
