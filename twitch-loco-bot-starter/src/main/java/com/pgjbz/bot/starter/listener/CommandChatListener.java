package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.command.enums.Command;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class CommandChatListener implements LocoChatListener {

    private final TwitchConnection twitchConnection;

    @Override
    public void listenChat(ChatMessage message) {
        Command command = extractCommand(message);
        if(nonNull(command))
            command.getStandardCommand().executeCommand(message, twitchConnection);
    }

    private static Command extractCommand(ChatMessage chatMessage){
        try {
            String message = chatMessage.getMessage();
            Pattern pattern = Pattern.compile("(?<=^!)([A-Za-z]+)(?!\\S)");
            Matcher matcher = pattern.matcher(message);
            if(matcher.find()) {
                return Command.valueOf(matcher.group().toUpperCase());
            }
        } catch (Exception e){
            log.warn("Unknown command {}", chatMessage.getMessage());
        }
        return null;
    }
}
