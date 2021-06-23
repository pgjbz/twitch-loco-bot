package com.pgjbz.twitch.loco.listeners.standards;

import com.pgjbz.twitch.loco.listeners.LocoChatListener;

public class StandardLocoChatListener implements LocoChatListener {

    @Override
    public void listenChat(String message) {
        String[] fields = message.split(":");
        String msg = fields[2];
        String channel = fields[1].substring(message.indexOf("#"));
        String user = message.split("!")[0].replace(":", "");
        System.out.println(user + " on " + channel + "-> " + msg);
    }
}
