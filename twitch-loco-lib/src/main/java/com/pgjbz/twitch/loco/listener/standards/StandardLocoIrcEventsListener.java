package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;

public class StandardLocoIrcEventsListener implements LocoIrcEventsListener {

    @Override
    public void listenEvent(String event) {
        System.out.println(event);
    }
}
