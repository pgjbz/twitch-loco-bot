package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.Ranking;

import java.util.List;

public interface RankingService {

    List<Ranking> findByChannel(String channel);

}
