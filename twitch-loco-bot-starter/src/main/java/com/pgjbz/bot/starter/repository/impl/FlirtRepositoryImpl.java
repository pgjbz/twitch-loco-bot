package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Flirt;
import com.pgjbz.bot.starter.repository.FlirtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class FlirtRepositoryImpl implements FlirtRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Flirt flirt) {
        String sql = "INSERT INTO FLIRTS (FLIRT) VALUES (?)";
        return jdbcTemplate.update(sql, new Object[]{flirt.getTeaser()}) > 0;
    }

    @Override
    public List<Flirt> findAll() {
        List<Flirt> flirts = new ArrayList<>();
        String sql = "SELECT ID, FLIRT FROM FLIRTS";
        try {
            flirts = jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultException e) {
            log.info("No teasers founded");
        }
        return flirts;
    }

    private final RowMapper<Flirt> rowMapper = (rs, row) -> {
        Long id = rs.getLong("ID");
        String teaser = rs.getString("FLIRT");
        return new Flirt(id, teaser);
    };
}
