package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.repository.TokenRepository;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TokensCommand implements StandardCommand {

    private final TokenRepository tokenRepository;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive tokens command {}", chatMessage.toString());
        try {
            tokenRepository.findByPk(new TokenPk(chatMessage.getUser(), chatMessage.getChannel())).ifPresentOrElse(
                token ->
                    twitchConnection.sendMessage(String.format("@%s you have %s tokens", chatMessage.getUser(), token.getUnit()))
                , () -> twitchConnection.sendMessage(String.format("%s you no have tokens", chatMessage.getUser()))
            );
        } catch (Exception e) {
            log.error("Error on send token command response {}", e.getMessage(), e);
        }
    }
}
