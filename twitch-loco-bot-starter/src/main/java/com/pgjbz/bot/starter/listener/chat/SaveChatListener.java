package com.pgjbz.bot.starter.listener.chat;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class SaveChatListener implements LocoChatListener {

    private final AbstractChatSaveChain chatSaveChain;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenChat(ChatMessage message) {
        if(nonNull(message))
            executorService.submit(() -> chatSaveChain.doChatSave(message));
    }
}
