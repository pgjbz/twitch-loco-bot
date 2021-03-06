package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.twitch.loco.enums.HttpMethod;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.util.HttpUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

import static com.pgjbz.bot.starter.configs.BotConstants.DECA_API;
import static com.pgjbz.bot.starter.configs.BotConstants.FOLLOWAGE_PATH;

@Log4j2
public class FollowAgeCommand implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive followage command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [followage] now");
            return;
        }

        final String followAgeUrl = DECA_API +
                FOLLOWAGE_PATH.replace("${channel}",
                chatMessage.getChannel()).replace("${username}", chatMessage.getUser());

        try {
            final String followAgeReturn = HttpUtil.execute(HttpMethod.GET, followAgeUrl);
            final String message = String.format("@%s follows %s for %s",
                    chatMessage.getUser(),
                    chatMessage.getChannel(),
                    followAgeReturn);
            twitchConnection.sendMessage(message);
        } catch (Exception e){
            log.error("Error on execute followage command on url {}", followAgeUrl, e);
        }
    }
}
