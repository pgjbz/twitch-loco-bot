package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaveChatListener implements LocoChatListener {

    private final AbstractChatSaveChain chatSaveChain;

    @Override
    public void listenChat(ChatMessage message) {
        chatSaveChain.doChatSave(message);
    }
}
