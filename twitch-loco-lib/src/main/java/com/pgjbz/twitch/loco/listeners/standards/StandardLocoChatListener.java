package com.pgjbz.twitch.loco.listeners.standards;

import com.pgjbz.twitch.loco.listeners.LocoChatListener;

public class StandardLocoChatListener implements LocoChatListener {

    @Override
    public void listenChat(String message) {
        System.out.println(message);
    }
}
