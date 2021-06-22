package com.pgjbz.twitch.loco.listeners.standards;

import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;

public class StandardLocoIrcEventsListener implements LocoIrcEventsListener {

    @Override
    public void listenEvent(String event) {
        System.out.println(event);
    }
}
