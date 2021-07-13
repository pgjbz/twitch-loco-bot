package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class DiceCommand implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        final Random random = new Random();
        log.info("Receive dice command {}", chatMessage.toString());
        twitchConnection.sendMessage(String.format("%s you roll %s", chatMessage.getUser(), (random.nextInt(6) + 1)));
    }

}
