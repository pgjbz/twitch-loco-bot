package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Flirt;
import com.pgjbz.bot.starter.repository.FlirtRepository;
import com.pgjbz.bot.starter.service.FlirtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class FlirtServiceImpl implements FlirtService {

    private final FlirtRepository flirtRepository;

    @Override
    public boolean insert(Flirt flirt) {
        boolean inserted = false;
        try {
            inserted = flirtRepository.insert(flirt);
        } catch (Exception e) {
            log.error("Error on insert new flirt {}", flirt.toString(), e);
        }
        return inserted;
    }

    @Override
    public List<Flirt> findAll() {
        return flirtRepository.findAll();
    }
}
