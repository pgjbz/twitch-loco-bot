package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Steal;
import com.pgjbz.bot.starter.repository.StealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class StealRepositoryImpl implements StealRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Steal steal) {
        String sql = "INSERT INTO STEALS(STEAL) VALUES (?)";
        return jdbcTemplate.update(sql, new Object[]{steal.getSteal()}) > 0;
    }

    @Override
    public List<Steal> findAll() {
        List<Steal> steals = new ArrayList<>();
        String sql = "SELECT ID, STEAL FROM STEALS";
        try {
            steals = jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultException e) {
            log.info("No steals founded");
        }
        return steals;
    }

    private final RowMapper<Steal> rowMapper = (rs, row) -> {
        Long id = rs.getLong("ID");
        String steal = rs.getString("STEAL");
        return new Steal(id, steal);
    };
}
