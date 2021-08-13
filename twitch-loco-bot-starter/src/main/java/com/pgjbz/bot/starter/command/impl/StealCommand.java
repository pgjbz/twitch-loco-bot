package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Steal;
import com.pgjbz.bot.starter.service.StealService;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class StealCommand implements StandardCommand {

    private final StealService stealService;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive steal command {}", chatMessage.toString());
        if(!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [steal] now");
            return;
        }
        final Random random = new Random();
        List<Steal> steals = stealService.findAll();
        if(!steals.isEmpty()) {
            String steal = steals.get(random.nextInt(steals.size())).getSteal();
            twitchConnection.sendMessage(BotUtils.formatMessage(steal, chatMessage));
        } else
            twitchConnection.sendMessage("Empty steals");
    }
}
