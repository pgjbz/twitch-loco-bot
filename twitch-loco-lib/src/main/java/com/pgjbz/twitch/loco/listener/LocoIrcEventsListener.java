package com.pgjbz.twitch.loco.listener;

import com.pgjbz.twitch.loco.model.IrcEvent;

public interface LocoIrcEventsListener {

    void listenEvent(IrcEvent event);

}
