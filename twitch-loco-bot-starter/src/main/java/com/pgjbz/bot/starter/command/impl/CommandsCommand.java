package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.command.enums.Command;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class CommandsCommand implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive commands command {}", chatMessage.toString());
        String commands = Stream.of(Command.values())
                .map(command -> "!" + command.name().toLowerCase())
                .collect(Collectors.joining(", "));
        twitchConnection.sendMessage("Command list: " + commands
                .replaceAll("[\\u005b-\\u005d]", ""));
    }

}