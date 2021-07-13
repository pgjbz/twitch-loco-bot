package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.Joke;
import com.pgjbz.bot.starter.repository.JokeRepository;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class JokeCommand implements StandardCommand {

    private final JokeRepository jokeRepository;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {
        log.info("Receive joke command {}", chatMessage.toString());
        final Random random = new Random();
        List<Joke> jokes = jokeRepository.findAll();
        if(!jokes.isEmpty())
            twitchConnection.sendMessage(jokes.get(random.nextInt(jokes.size())).getJoke());
        else
            twitchConnection.sendMessage("Empty jokes");
    }
}
