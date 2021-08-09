package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Ranking;
import com.pgjbz.bot.starter.repository.RankingRepository;
import com.pgjbz.bot.starter.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingRepository rankingRepository;

    @Override
    public List<Ranking> findByChannel(String channel) {
        try {
            return rankingRepository.findByChannel(channel);
        } catch (Exception e) {
            log.error("Error on find ranking by channel {}", channel, e);
        }
        return new ArrayList<>();
    }

}
