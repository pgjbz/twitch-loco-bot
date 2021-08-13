package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.BotResponse;
import com.pgjbz.bot.starter.repository.BotResponseRepository;
import com.pgjbz.bot.starter.service.BotResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class BotResponseServiceImpl implements BotResponseService {

    private final BotResponseRepository botResponseRepository;

    @Override
    public boolean insert(BotResponse botResponse) {
        boolean inserted = false;
        try {
            inserted = botResponseRepository.insert(botResponse);
        } catch (Exception e) {
            log.error("Error on insert new bot response {}", botResponse.toString(), e);
        }
        return inserted;
    }

    @Override
    public List<BotResponse> findAll() {
        try {
            return botResponseRepository.findAll();
        } catch (Exception e) {
            log.error("Error on find all", e);
        }
        return new ArrayList<>();
    }
}
