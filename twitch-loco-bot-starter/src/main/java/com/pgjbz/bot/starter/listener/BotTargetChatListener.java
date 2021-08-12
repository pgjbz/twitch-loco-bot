package com.pgjbz.bot.starter.listener;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@RequiredArgsConstructor
public class BotTargetChatListener implements LocoChatListener {

    private final TwitchConnection twitchConnection;

    private final static ExecutorService executors = Executors.newSingleThreadExecutor();

    @Override
    public void listenChat(ChatMessage message) {
        if(!message.getMessage().toLowerCase().contains(twitchConnection.getBotName().toLowerCase())) return;
        log.info("{} sends message with bot target", message.getUser());
        executors.submit(() -> twitchConnection.sendMessage(String.format("@%s who are u?", message.getUser())));
    }
}
