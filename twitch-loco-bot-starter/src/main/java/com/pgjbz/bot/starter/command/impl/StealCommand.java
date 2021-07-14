package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Steal;
import com.pgjbz.bot.starter.repository.StealRepository;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class StealCommand implements StandardCommand {

    private final StealRepository stealRepository;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive steal command {}", chatMessage.toString());
        final Random random = new Random();
        List<Steal> steals = stealRepository.findAll();
        if(!steals.isEmpty()) {
            String steal = steals.get(random.nextInt(steals.size())).getSteal();
            twitchConnection.sendMessage(BotUtils.formatCommand(steal, chatMessage));
        } else
            twitchConnection.sendMessage("Empty steals");
    }
}
