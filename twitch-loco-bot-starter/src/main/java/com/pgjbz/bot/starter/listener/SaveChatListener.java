package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class SaveChatListener implements LocoChatListener {

    private final AbstractChatSaveChain chatSaveChain;

    @Override
    public void listenChat(ChatMessage message) {
        if(nonNull(message))
            chatSaveChain.doChatSave(message);
    }
}
