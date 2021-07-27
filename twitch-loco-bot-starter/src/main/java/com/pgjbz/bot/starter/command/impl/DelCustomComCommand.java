package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.service.CustomCommandService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RequiredArgsConstructor
public class DelCustomComCommand implements StandardCommand {

    private final CustomCommandService customCommandService;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("commmand [delcustomcom] receive, perform command delete");
        if(!twitchConnection.getModsList().contains(chatMessage.getUser()) &&
                !twitchConnection.getChannel().equalsIgnoreCase(chatMessage.getUser())) return;
        String commandToDelete = extractDeleteCommand(chatMessage.getMessage()).toLowerCase(Locale.ROOT);
        customCommandService.findByChannelAndCommand(chatMessage.getChannel(), commandToDelete).ifPresent(
                customCommand -> {
                    log.info("Trying to delete custom command {}", commandToDelete);
                    if(customCommandService.delete(customCommand))
                        twitchConnection.sendMessage(String.format("Command %s deleted", commandToDelete));
                }
        );
    }

    private String extractDeleteCommand(String message) {
        Pattern pattern = Pattern.compile("(?<=\\s!)[A-Za-z]+(?!\\S)");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }
}
