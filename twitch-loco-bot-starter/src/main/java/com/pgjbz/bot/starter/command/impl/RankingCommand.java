package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Ranking;
import com.pgjbz.bot.starter.service.RankingService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public record RankingCommand(RankingService rankingService) implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {

        log.info("Receive ranking command {}", chatMessage.toString());
        if (!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [joke] now");
            return;
        }

        List<Ranking> rankingList = rankingService.findByChannel(chatMessage.channel());

        if (rankingList.isEmpty()) {
            twitchConnection.sendMessage("No ranking for this channel");
            return;
        }

        String ranking = rankingList.stream().map(rank -> new StringBuilder()
                        .append(rank.getPosition())
                        .append(" - ")
                        .append(rank.getUsername())
                        .append(" has ")
                        .append(rank.getUnit())
                        .append(" tokens"))
                .collect(Collectors.joining(", "));

        twitchConnection.sendMessage("Tokens ranking: " + ranking);

    }
}
