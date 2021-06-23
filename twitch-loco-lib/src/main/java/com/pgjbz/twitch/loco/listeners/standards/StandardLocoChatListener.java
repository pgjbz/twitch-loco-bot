package com.pgjbz.twitch.loco.listeners.standards;

import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.util.ChatUtil;

public class StandardLocoChatListener implements LocoChatListener {

    @Override
    public void listenChat(String message) {
        ChatMessage chatMessage = ChatUtil.extractFields(message);
        System.out.println(chatMessage.getUser() + " on " + chatMessage.getChannel() + "-> " + chatMessage.getMessage());
    }
}
