package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Bot;
import com.pgjbz.bot.starter.repository.BotRepository;
import com.pgjbz.bot.starter.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final BotRepository botRepository;

    @Override
    public boolean insert(Bot bot) {
        boolean inserted = false;
        try {
            inserted =  botRepository.insert(bot);
        } catch (Exception e){
            log.error("Error on insert new bot {}", bot.toString(), e);
        }
        return inserted;
    }

    @Override
    public List<Bot> findAll() {
        List<Bot> bots = Collections.singletonList(new Bot(999L, "Bot97loco"));
        try {
            bots = botRepository.findAll();
        } catch (Exception e) {
            log.error("Error on find all bots", e);
        }
        return bots;
    }
}
