package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.service.TokenService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Date;

@Log4j2
@RequiredArgsConstructor
public class TokensCommand implements StandardCommand {

    private final TokenService tokenService;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive tokens command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [tokens] now");
            return;
        }
        try {
            tokenService.findByPk(new TokenPk(chatMessage.getUser(), chatMessage.getChannel())).ifPresentOrElse(
                token ->
                    twitchConnection.sendMessage(String.format("@%s you have %s tokens", chatMessage.getUser(), token.getUnit()))
                , () -> twitchConnection.sendMessage(String.format("%s you no have tokens", chatMessage.getUser()))
            );
        } catch (Exception e) {
            log.error("Error on send token command response {}", e.getMessage(), e);
        }
    }
}
