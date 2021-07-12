package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
public class StandardLocoIrcEventsListener implements LocoIrcEventsListener {


    /*
      Standard Bot stream info listener only log irc event
   */
    @Override
    public void listenEvent(IrcEvent event) {
        if(nonNull(event))
            log.info(event);
    }
}
