package com.pgjbz.bot.starter.repository.impl;

import com.pgjbz.bot.starter.database.exception.EmptyResultException;
import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.database.jdbc.RowMapper;
import com.pgjbz.bot.starter.model.Joke;
import com.pgjbz.bot.starter.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class JokeRepositoryImpl implements JokeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean insert(Joke joke) {
        String sql = "INSERT INTO JOKES(JOKE) VALUES (?)";
        return jdbcTemplate.update(sql, new Object[]{joke.getJoke()}) > 0;
    }

    @Override
    public List<Joke> findAll() {
        List<Joke> jokes = new ArrayList<>();
        String sql = "SELECT ID, JOKE FROM JOKES";
        try {
            jokes = jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultException e) {
            log.info("Empty joke list");
        }
        return jokes;
    }

    private final RowMapper<Joke> rowMapper = (rs, row) -> {
        Long id = rs.getLong("ID");
        String joke = rs.getString("JOKE");
        return new Joke(id, joke);
    };
}
