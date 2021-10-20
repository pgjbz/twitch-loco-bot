package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Flirt;
import com.pgjbz.bot.starter.service.FlirtService;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
public record FlirtCommand(FlirtService flirtService) implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive flirt command {}", chatMessage.toString());
        if (!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [flirt] now");
            return;
        }
        final Random random = new Random();
        List<Flirt> flirts = flirtService.findAll();
        if (!flirts.isEmpty()) {
            String teaser = flirts.get(random.nextInt(flirts.size())).getTeaser();
            twitchConnection.sendMessage(BotUtils.formatMessage(teaser, chatMessage));
        } else
            twitchConnection.sendMessage("Flirts is empty");
    }
}
