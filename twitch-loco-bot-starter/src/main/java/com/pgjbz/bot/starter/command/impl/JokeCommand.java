package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Joke;
import com.pgjbz.bot.starter.service.JokeService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Log4j2
public record JokeCommand(JokeService jokeService) implements StandardCommand {

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive joke command {}", chatMessage.toString());
        if (!twitchConnection.canSendMessage(new Date(System.currentTimeMillis()), false)) {
            log.info("Cannot perform command [joke] now");
            return;
        }
        final Random random = new Random();
        List<Joke> jokes = jokeService.findAll();
        if (!jokes.isEmpty())
            twitchConnection.sendMessage(jokes.get(random.nextInt(jokes.size())).getJoke());
        else
            twitchConnection.sendMessage("Empty jokes");
    }
}
