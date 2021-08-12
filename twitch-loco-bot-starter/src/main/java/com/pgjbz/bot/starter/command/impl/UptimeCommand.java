package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.twitch.loco.enums.HttpMethod;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.util.HttpUtil;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

import static com.pgjbz.bot.starter.configs.BotConstants.UPTIME_URL;

@Log4j2
public class UptimeCommand implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive online command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [online] now");
            return;
        }
        final String uptimeUrl = UPTIME_URL.replace("{channel}", chatMessage.getChannel());
        try {
            final String uptimeReturn = HttpUtil.execute(HttpMethod.GET, uptimeUrl);
            final String message = String.format("@%s channel %s uptime -> %s",
                    chatMessage.getUser(),
                    chatMessage.getChannel(),
                    uptimeReturn);
            twitchConnection.sendMessage(message);
        } catch (Exception e){
            log.error("Error on execute get command on url {}", uptimeUrl, e);
        }
    }
}
