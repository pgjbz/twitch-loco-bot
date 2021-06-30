package com.pgjbz.twitch.loco.listener;

import com.pgjbz.twitch.loco.model.ChatMessage;

public interface LocoChatListener {

    void listenChat(ChatMessage message);

}
