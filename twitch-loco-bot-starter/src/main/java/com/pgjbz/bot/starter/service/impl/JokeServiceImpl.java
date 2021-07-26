package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Joke;
import com.pgjbz.bot.starter.repository.JokeRepository;
import com.pgjbz.bot.starter.service.JokeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;

    @Override
    public boolean insert(Joke joke) {
        boolean inserted = false;
        try {
            inserted = jokeRepository.insert(joke);
        } catch (Exception e) {
            log.error("Error on insert new joke {}", joke.toString(), e);
        }
        return inserted;
    }

    @Override
    public List<Joke> findAll() {
        List<Joke> jokes = new ArrayList<>();
        try {
            jokes =  jokeRepository.findAll();
        } catch (Exception e) {
            log.info("Error on find all jokes", e);
        }
        return jokes;
    }
}
