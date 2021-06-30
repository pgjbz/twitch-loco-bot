package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;

public class StandardLocoChatListener implements LocoChatListener {

    @Override
    public void listenChat(ChatMessage message) {
        System.out.println(message.getUser() + " on " + message.getChannel() + " -> " + message.getMessage());
    }
}
