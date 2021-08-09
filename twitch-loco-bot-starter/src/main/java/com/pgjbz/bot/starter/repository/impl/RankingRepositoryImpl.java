package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Ranking;
import com.pgjbz.bot.starter.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Ranking> findByChannel(String channel) {
        List<Ranking> ranking = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  ");
        sql.append("   USERNAME, ");
        sql.append("   UNIT, ");
        sql.append("   ROW_NUMBER() OVER (ORDER BY UNIT DESC) as POSITION ");
        sql.append(" FROM ");
        sql.append("   TOKENS ");
        sql.append(" WHERE  ");
        sql.append("   CHANNEL = ? ");
        sql.append(" ORDER BY  ");
        sql.append("   UNIT DESC ");
        sql.append(" LIMIT 10; ");
        try {
            ranking = jdbcTemplate.query(sql.toString(), new Object[]{channel}, rowMapper);
        } catch (EmptyResultException e) {
            log.info("No results founded for ranking in channel {}", channel);
        }
        return ranking;
    }

    private final RowMapper<Ranking> rowMapper = (rs, row) -> {
        String username = rs.getString("USERNAME");
        Long units = rs.getLong("UNIT");
        Byte position = rs.getByte("POSITION");
        return new Ranking(username, units, position);
    };
}
