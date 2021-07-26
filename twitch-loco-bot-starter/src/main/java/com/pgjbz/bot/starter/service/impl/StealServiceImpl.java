package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Steal;
import com.pgjbz.bot.starter.repository.StealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class StealServiceImpl implements com.pgjbz.bot.starter.service.StealService {

    private final StealRepository stealRepository;

    @Override
    public boolean insert(Steal steal) {
        boolean inserted = false;
        try {
            inserted =  stealRepository.insert(steal);
        } catch (Exception e) {
            log.error("Error on insert new steal", e);
        }
        return inserted;
    }

    @Override
    public List<Steal> findAll() {
        return stealRepository.findAll();
    }
}
