package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

import static com.pgjbz.bot.starter.configs.BotConstants.BOT_CREATOR;
import static com.pgjbz.bot.starter.configs.BotConstants.BOT_REPOSITORY;

@Log4j2
public class BotCommand implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive commands command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [commands] now");
            return;
        }
        twitchConnection.sendMessage(String.format("Bot repository: %s bot creator: %s", BOT_REPOSITORY, BOT_CREATOR));
    }

}
