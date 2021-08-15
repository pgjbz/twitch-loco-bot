package com.pgjbz.bot.starter.listener.chat;

import com.pgjbz.bot.starter.model.BotResponse;
import com.pgjbz.bot.starter.service.BotResponseService;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@RequiredArgsConstructor
public class BotTargetChatListener implements LocoChatListener {

    private final TwitchConnection twitchConnection;
    private final BotResponseService botResponseService;

    private final static ExecutorService executors = Executors.newSingleThreadExecutor();
    private final Random random = new Random();

    @Override
    public void listenChat(ChatMessage chatMessage) {
        if(!chatMessage.getMessage().toLowerCase().contains(twitchConnection.getBotName().toLowerCase())
            || chatMessage.getUser().toLowerCase().contains(twitchConnection.getBotName().toLowerCase())) return;
        log.info("{} receive message with bot target", chatMessage.getUser());

        executors.submit(() -> {
            final List<BotResponse> botResponses = botResponseService.findAll();
            if(botResponses.isEmpty()) {
                log.info("No messages are registered");
                return;
            }
            final String message = botResponses.get(random.nextInt(botResponses.size())).getMessage();
            twitchConnection.sendMessage(BotUtils.formatMessage(message, chatMessage));
        });
    }
}
