package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.command.enums.Command;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class CommandChatListener implements LocoChatListener {

    private final TwitchConnection twitchConnection;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenChat(ChatMessage message) {
        executorService.submit(() -> {
            Command command = extractCommand(message);
            if(nonNull(command))
                command.getStandardCommand().executeCommand(message, twitchConnection);
        });
    }

    private static Command extractCommand(ChatMessage chatMessage){
        try {
            String command =  BotUtils.extractCommandFromMessage(chatMessage);
            if(StringUtils.isNotBlank(command))
                return Command.valueOf(command.toUpperCase());
        } catch (Exception e){
            log.warn("Unknown command {}", chatMessage.getMessage());
        }
        return null;
    }
}
