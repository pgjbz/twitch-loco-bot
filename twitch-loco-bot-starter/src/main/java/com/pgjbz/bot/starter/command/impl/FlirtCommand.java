package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Flirt;
import com.pgjbz.bot.starter.repository.FlirtRepository;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Log4j2
@RequiredArgsConstructor
public class FlirtCommand implements StandardCommand {

    private final FlirtRepository flirtRepository;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive flirt command {}", chatMessage.toString());
        final Random random = new Random();
        List<Flirt> flirts = flirtRepository.findAll();
        if(!flirts.isEmpty()) {
            String teaser = flirts.get(random.nextInt(flirts.size())).getTeaser();
            String target = BotUtils.extractTarget(chatMessage.getMessage());
            twitchConnection.sendMessage(teaser
                    .replace("${target}", isBlank(target) ? chatMessage.getUser() : target));
        } else
            twitchConnection.sendMessage("Teasers is empty");
    }
}
