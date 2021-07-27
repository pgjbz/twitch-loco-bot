package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.command.enums.Command;
import com.pgjbz.bot.starter.service.CustomCommandService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class CommandsCommand implements StandardCommand {

    private final CustomCommandService customCommandService;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive commands command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [commands] now");
            return;
        }
        String commands = Stream.of(Command.values())
                .map(command -> "!" + command.name().toLowerCase())
                .collect(Collectors.joining(", "))
                .replaceAll("[\\u005b-\\u005d]", "") ////remove []
        + ", " + customCommandService.findByChannel(chatMessage.getChannel())
                .stream().map(customCommand -> "!"+customCommand.getCommand())
                .collect(Collectors.joining(", "))
                .replaceAll("[\\u005b-\\u005d]", ""); ////remove []
        twitchConnection.sendMessage(commands);
    }

}
