package com.pgjbz.twitch.loco.listeners;

import com.pgjbz.twitch.loco.model.ChatMessage;

public interface LocoChatListener {

    void listenChat(ChatMessage message);

}
