package com.pgjbz.bot.starter.repository;

import com.pgjbz.bot.starter.model.Ranking;

import java.util.List;

public interface RankingRepository {

    List<Ranking> findByChannel(String channel);

}
